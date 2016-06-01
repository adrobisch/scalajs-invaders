package invaders

import scala.collection.mutable
import scala.scalajs.js
import scala.scalajs.js.{JSApp, UndefOr, |}
import scala.scalajs.js.annotation.{JSExportAll, JSName}

@JSName("Phaser")
@js.native
object Phaser extends js.Object {
  val AUTO: Int = js.native //0

  @JSName("Physics")
  @js.native
  object Physics extends js.Object {
    val ARCADE: Int = js.native // 0
  }

  @JSName("Key")
  @js.native
  trait Key extends js.Object {
    def isDown: Boolean = js.native
  }

  @JSName("Keyboard")
  @js.native
  object Keyboard extends js.Object{
    val SPACEBAR: Int = js.native // 32
  }

  @JSName("Easing")
  @js.native
  object Easing extends js.Object {
    @JSName("Linear")
    @js.native
    object Linear extends js.Object {
      val None: js.Function1[_, _] = js.native
    }
  }
}

@JSName("Phaser.Game")
@js.native
class Game(val width: Int = 800,
           val height: Int = 600,
           val renderer: Int = Phaser.AUTO,
           val name: String = "phaser-game"
) extends js.Object {
  val load: Loader = js.native
  val state: State = js.native
  val physics: Physics = js.native
  val add: GameObjectFactory = js.native
  val world: World = js.native
  val input: Input = js.native
  val time: Time = js.native
  val rnd: RandomDataGenerator = js.native
}

@js.native
trait RandomDataGenerator extends js.Object {
  def integerInRange(min: Int, max: Int): Int = js.native
}

@js.native
trait Time extends js.Object {
  val now: Double = js.native
}

@js.native
trait Core extends js.Object {
  val animations: AnimationManager = js.native
}

@js.native
trait Lifespan extends js.Object {
  val alive: Boolean = js.native
  def kill(): DisplayObject = js.native
  def revive(health: Double = 1): DisplayObject = js.native
}

@js.native
trait Reset extends js.Object {
  def reset(x: Double, y: Double, health: Double = 1): DisplayObject = js.native
}

@js.native
trait AnimationManager extends js.Object {
  def add(name: String, frames: js.Array[_] = js.Array(), frameRate: UndefOr[Int] = 60, loop: UndefOr[Boolean] = false, useNumericIndex: UndefOr[Boolean] = true): Animation = js.native
}

@js.native
trait Input extends js.Object{
  val keyboard: Keyboard = js.native
  val onTap: Signal
}

@js.native
trait Keyboard extends js.Object {
  def addKey(code: Int): Phaser.Key = js.native
  def createCursorKeys(): CursorKeys = js.native
}

@js.native
trait World extends js.Object {
  var width: Double = js.native
  def centerX: Double = js.native
  def centerY: Double = js.native
}

@js.native
trait Loader extends js.Object {
  def image(key: String, url: String)
  def spritesheet(key: String, url: String, frameWidth: Int, frameHeight: Int, frameMax: Int = -1, margin: Int = 0, spacing: Int = 0)
}

@js.native
trait State extends js.Object {
  def add(name: String, state: Any): Unit = js.native
  def start(name: String): Unit = js.native
}

@js.native
trait Physics extends js.Object {
  def enable(player: Sprite, system: Int, debug: Boolean = false): Unit = js.native

  def startSystem(systemId: Int): Unit = js.native

  val arcade: ArcadePhysics = js.native
}

@js.native
trait ArcadePhysics extends js.Object {
  def overlap(object1: Sprite | Group[_] | js.Array[_],
              object2: Sprite | Group[_] | js.Array[_],
              overlapCallback: js.Function2[_, _ , _],
              processCallback: Option[js.Function2[_, _ , Boolean]],
              callbackContext: Any = this): Unit = js.native

  def moveToObject(displayObject: DisplayObject, destination: Any, speed: Int = 60, maxTime: Long = 0): Double = js.native
}

@js.native
trait GameObjectFactory extends js.Object {
  def tween(aliens: DisplayObject): Tween = js.native

  def text(x: Double = 0, y: Double = 0, text: String = "", style: js.Dictionary[_] = null, group: Group[_] = null): Text = js.native

  def sprite(x: Int = 0, y: Int = 0, key: String = null, frame: Any = null, group: Group[_] = null): Sprite
  def group(parent: Any = null, name: String = null, addToStage: Boolean  = false, enableBody: Boolean = false, physicsBodyType: Int = 0): Group[Sprite] = js.native
  def tileSprite(x: Int, y: Int, width: Int, height: Int, key: String, frame: Any = null, group: Group[_] = null): TileSprite = js.native
}

@js.native
trait Tween extends js.Object {
  def to(properties: js.Dictionary[_], duration: Int = 1000, ease: Any = null, autoStart: Boolean = false, delay: Int = 0, repeat: Int = 0, yoyo: Boolean = false): Tween = js.native
  def onLoop: Signal = js.native
}

@js.native
trait Signal extends js.Object {
  def addOnce(listener: js.ThisFunction0[_, _], listenerContext: Any, priority: Int, args: Any*): SignalBinding = js.native
  def addOnce(listener: js.ThisFunction0[_, _], listenerContext: Any = this, priority: Int = 0): SignalBinding = js.native
  def add(listener: js.ThisFunction0[_, _], listenerContext: Any, priority: Int, args: Any*): SignalBinding = js.native
  def add(listener: js.ThisFunction0[_, _], listenerContext: Any = this, priority: Int = 0): SignalBinding = js.native
}

@js.native
trait SignalBinding extends js.Object

@js.native
trait Group[T] extends DisplayObjectContainer {
  def getFirstAlive(): UndefOr[T] = js.native

  def getFirstAlive(createIfNull: Boolean = false, x: Double, y: Double, key: String = null, frame: String | Int = null): UndefOr[T] = js.native

  def removeAll(destroy: Boolean = false, silent: Boolean = false): Unit = js.native

  def countLiving(): Int = js.native

  def getFirstExists(exists: Boolean): UndefOr[T] = js.native

  def forEach(callback: js.Function1[T, Any], callbackContext: Any = this, checkExists: Boolean = false): Unit = js.native
  def forEach(callback: js.Function1[T, Any], callbackContext: Any, checkExists: Boolean, args: Any*): Unit = js.native
  def forEachAlive(callback: js.Function1[T, Any], callbackContext: Any, args: Any*): Unit = js.native

  def create(x: Double, y: Double, key: Any = null, frame: Any = null, exists: Boolean = true): T = js.native

  def setAll(key: String, value: Any, checkAlive: Boolean = false, checkVisible: Boolean = false, operation: Int = 0, force: Boolean = false): Unit = js.native

  def createMultiple(quantity: Int, key: String, frame: Any = null, exists: Boolean = false): Unit = js.native

  var enableBody: Boolean = js.native
  var physicsBodyType: Int = js.native
}

@js.native
trait Text extends Sprite {
  var text: String = js.native
}

@JSName("PIXI.Point")
@js.native
trait Point extends js.Object {
  var x: Double = js.native
  var y: Double = js.native

  def setTo(x: Double, y: Double ): Point = js.native
}


@JSName("PIXI.DisplayObject")
@js.native
trait DisplayObject extends js.Object {
  var x: Double = js.native
  var y: Double = js.native

  var angle: Double = js.native
  var alpha: Double = js.native

  var visible: Boolean = js.native
  val anchor: Point = js.native
}

@JSName("PIXI.DisplayObjectContainer")
@js.native
trait DisplayObjectContainer extends DisplayObject

@js.native
trait Sprite extends Core with DisplayObject with Lifespan with Reset {
  var body: PhysicsBody = js.native
  def play(key: String, frameRate: Int = 60, loop: Boolean = false, killOnComplete: Boolean = false): Animation = js.native
}

@js.native
trait PhysicsBody extends js.Object {
  val x: Double = js.native
  val y: Double = js.native
  var moves: Boolean = js.native // true
  val velocity: Point = js.native
}

@js.native
trait Animation extends js.Object {
  def play(frameRate: Int = 60, loop: Boolean = false, killOnComplete: Boolean = false): Animation = js.native
}

@js.native
trait TileSprite extends js.Object {
  val tilePosition: Point = js.native
}

trait GameState {
  def preload(): Unit
  def create(): Unit
  def update(): Unit
  def render(): Unit
}

@js.native
class CursorKeys extends js.Object {
  def up: Phaser.Key = js.native
  def down: Phaser.Key = js.native
  def left: Phaser.Key = js.native
  def right: Phaser.Key = js.native
}

@JSExportAll
class InvadersGameState(val game: Game) extends GameState {
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
      game.input.onTap.addOnce(restart,this)
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
    }, this)

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

  override def render(): Unit = {

  }
}

object InvadersApp extends JSApp {
  def main(): Unit = {
    val game = new Game()
    game.state.add("initial", new InvadersGameState(game))
    game.state.start("initial")
  }
}
