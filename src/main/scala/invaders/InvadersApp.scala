package invaders

import scala.collection.mutable
import scala.scalajs.js
import scala.scalajs.js.{JSApp, UndefOr, |}
import scala.scalajs.js.annotation.{JSExportAll, JSName}

import phaser._
import phaser.Phaser._

@JSExportAll
class InvadersGame(val game: Phaser.Game) extends GameState {
  var player: Sprite = null
  var aliens: Group[Sprite] = null
  var bullets: Group[Sprite] = null
  var bulletTime: Double = 0
  var cursors: CursorKeys = null
  var fireButton: Phaser.Key = null
  var explosions: Group[Sprite] = null
  var starfield: TileSprite = null
  var score: Int = 0
  var scoreString = ""
  var scoreText: Text = null
  var lives: Group[Sprite] = null
  var enemyBullets: Group[Sprite] = null
  var firingTimer: Double = 0
  var stateText: Text = null
  var livingEnemies: mutable.Seq[Sprite] = mutable.Seq()

  def preload() = {
    game.load.image("bullet", "assets/bullet.png")
    game.load.image("enemyBullet", "assets/enemy-bullet.png")
    game.load.spritesheet("invader", "assets/invader32x32x4.png", 32, 32)
    game.load.image("ship", "assets/player.png")
    game.load.spritesheet("kaboom", "assets/explode.png", 128, 128)
    game.load.image("starfield", "assets/starfield.png")
    game.load.image("background", "assets/background2.png")
  }

  def create() : Unit = {
    game.physics.startSystem(Phaser.Physics.ARCADE)

    //  The scrolling starfield background
    starfield = game.add.tileSprite(0, 0, 800, 600, "starfield")

    //  Our bullet group
    bullets = game.add.group()
    bullets.enableBody = true
    bullets.physicsBodyType = Phaser.Physics.ARCADE
    bullets.createMultiple(30, "bullet")
    bullets.setAll("anchor.x", 0.5)
    bullets.setAll("anchor.y", 1)
    bullets.setAll("outOfBoundsKill", true)
    bullets.setAll("checkWorldBounds", true)

    // The enemy's bullets
    enemyBullets = game.add.group()
    enemyBullets.enableBody = true
    enemyBullets.physicsBodyType = Phaser.Physics.ARCADE
    enemyBullets.createMultiple(30, "enemyBullet")
    enemyBullets.setAll("anchor.x", 0.5)
    enemyBullets.setAll("anchor.y", 1)
    enemyBullets.setAll("outOfBoundsKill", true)
    enemyBullets.setAll("checkWorldBounds", true)

    //  The hero!
    player = game.add.sprite(400, 500, "ship")
    player.anchor.setTo(0.5, 0.5)
    game.physics.enable(player, Phaser.Physics.ARCADE)

    //  The baddies!
    aliens = game.add.group()
    aliens.enableBody = true
    aliens.physicsBodyType = Phaser.Physics.ARCADE

    createAliens()

    //  The score
    scoreString = "Score : "
    scoreText = game.add.text(10, 10, scoreString + score, js.Dictionary("font" -> "34px Arial", "fill" -> "#fff"))

    //  Lives
    lives = game.add.group()
    game.add.text(game.world.width - 100, 10, "Lives : ", js.Dictionary("font" -> "34px Arial", "fill" -> "#fff"))

    //  Text
    stateText = game.add.text(game.world.centerX,game.world.centerY," ", js.Dictionary("font" -> "84px Arial", "fill" -> "#fff" ))
    stateText.anchor.setTo(0.5, 0.5)
    stateText.visible = false

    for (i <- 0 to 2) {
      val ship = lives.create(game.world.width - 100 + (30 * i), 60, "ship")
      ship.anchor.setTo(0.5, 0.5)
      ship.angle = 90
      ship.alpha = 0.4
    }

    //  An explosion pool
    explosions = game.add.group()
    explosions.createMultiple(30, "kaboom")
    explosions.forEach(setupInvader)

    //  And some controls to play the game with
    cursors = game.input.keyboard.createCursorKeys()
    fireButton = game.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR)
  }

  def createAliens() = {
    for (y <- 0 to 3)
    {
      for (x <- 0 to 9)
      {
        var alien = aliens.create(x * 48, y * 50, "invader")
        alien.anchor.setTo(0.5, 0.5)
        alien.animations.add("fly", js.Array(0, 1, 2, 3), 20, true)
        alien.play("fly")
       // alien.body.moves = false
      }
    }

    aliens.x = 100
    aliens.y = 50

    //  All this does is basically start the invaders moving. Notice we're moving the Group they belong to, rather than the invaders directly.
    var tween = game.add.tween(aliens).to(js.Dictionary("x" -> 200), 2000, Phaser.Easing.Linear.None, true, 0, 1000, true)

    //  When the tween loops it calls descend
    tween.onLoop.add(descend)
  }

  def descend = { foo: Any =>
    aliens.y += 10
  }

  def setupInvader = { invader: Sprite =>
    invader.anchor.x = 0.5
    invader.anchor.y = 0.5
    invader.animations.add("kaboom")
  }

  override def update(): Unit = {
    //  Scroll the background
    starfield.tilePosition.y += 2

    if (player.alive)
    {
      //  Reset the player, then check for movement keys
      player.body.velocity.setTo(0, 0)

      if (cursors.left.isDown)
      {
        player.body.velocity.x = -200
      }
      else if (cursors.right.isDown)
      {
        player.body.velocity.x = 200
      }

      //  Firing?
      if (fireButton.isDown)
      {
        fireBullet()
      }

      if (game.time.now > firingTimer)
      {
        enemyFires()
      }
      //  Run collision
      game.physics.arcade.overlap(bullets, aliens, collisionHandler, null, this)
      game.physics.arcade.overlap(enemyBullets, player, enemyHitsPlayer, null, this)
    }
  }

  def enemyHitsPlayer = { (player: Sprite, bullet: Sprite) =>

    bullet.kill()

    val live = lives.getFirstAlive()

    if (live.isDefined)
    {
      live.get.kill()
    }

    //  And create an explosion :)
    var explosion = explosions.getFirstExists(false)
    explosion.get.reset(player.body.x, player.body.y)
    explosion.get.play("kaboom", 30, false, true)

    // When the player dies
    if (lives.countLiving() < 1)
    {
      player.kill()
      enemyBullets.forEach({bullet: Sprite => bullet.kill()})

      stateText.text=" GAME OVER \n Click to restart"
      stateText.visible = true

      //the "click to restart" handler
      game.input.onTap.addOnce(restart)
    }
  }

  def collisionHandler = { (bullet: Sprite, alien: Sprite) =>

    //  When a bullet hits an alien we kill them both
    bullet.kill()
    alien.kill()

    //  Increase the score
    score += 20
    scoreText.text = scoreString + score

    //  And create an explosion :)
    var explosion = explosions.getFirstExists(false)
    explosion.get.reset(alien.body.x, alien.body.y)
    explosion.get.play("kaboom", 30, loop = false, killOnComplete = true)

    if (aliens.countLiving() == 0)
    {
      score += 1000
      scoreText.text = scoreString + score

      enemyBullets.forEach({ sprite: Sprite =>
        sprite.kill()
      })

      stateText.text = " You Won, \n Click to restart"
      stateText.visible = true

      game.input.onTap.addOnce(restart)
    }
  }

  def restart = { context: Any =>
    //  A new level starts

    //resets the life count
    lives.forEach({ live: Sprite => live.revive() })
    //  And brings the aliens back from the dead :)
    aliens.removeAll()
    createAliens()

    //revives the player
    player.revive()
    //hides the text
    stateText.visible = false
  }

  def fireBullet() = {

    //  To avoid them being allowed to fire too fast we set a time limit
    if (game.time.now > bulletTime)
    {
      //  Grab the first bullet we can from the pool
      val bullet: UndefOr[Sprite] = bullets.getFirstExists(false)

      if (bullet.isDefined)
      {
        //  And fire it
        val bulletSprite: Sprite = bullet.get

        bulletSprite.reset(player.x, player.y + 8)
        bulletSprite.body.velocity.y = -400

        bulletTime = game.time.now + 200
      }
    }

  }

  def enemyFires() = {

    //  Grab the first bullet we can from the pool
    val enemyBullet = enemyBullets.getFirstExists(false)

    livingEnemies = mutable.Seq()

    aliens.forEachAlive({ alien: Sprite =>
      livingEnemies :+= alien
    })

    if (enemyBullet.isDefined && livingEnemies.length > 0)
    {

      val random: Int = game.rnd.integerInRange(0, livingEnemies.length-1)

      // randomly select one of them
      var shooter: Sprite = livingEnemies(random)
      // And fire the bullet from this enemy
      enemyBullet.get.reset(shooter.body.x, shooter.body.y)

      game.physics.arcade.moveToObject(enemyBullet.get, player, 120)
      firingTimer = game.time.now + 2000
    }

  }

  override def render(): Unit = {}
}

object InvadersApp extends JSApp {
  def main(): Unit = {
    val game = new Phaser.Game(800, 600, Phaser.AUTO, "invaders-game")
    game.state.add("initial", new InvadersGame(game))
    game.state.start("initial")
  }
}
