package phaser

import scala.collection.mutable
import scala.scalajs.js
import scala.scalajs.js.{JSApp, UndefOr, |}
import scala.scalajs.js.annotation.{JSExportAll, JSName}

trait GameState {
  def preload(): Unit
  def create(): Unit
  def update(): Unit
  def render(): Unit
}

@JSName("Phaser")
@js.native
object Phaser extends js.Object {
  import PIXI._

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

  @JSName("Game")
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

    def forEachAlive(callback: js.Function1[T, Any], callbackContext: Any = this): Unit = js.native
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

  @js.native
  class CursorKeys extends js.Object {
    def up: Phaser.Key = js.native
    def down: Phaser.Key = js.native
    def left: Phaser.Key = js.native
    def right: Phaser.Key = js.native
  }
}

@JSName("PIXI")
@js.native
object PIXI extends js.Object {
  @JSName("Point")
  @js.native
  trait Point extends js.Object {
    var x: Double = js.native
    var y: Double = js.native

    def setTo(x: Double, y: Double ): Point = js.native
  }

  @JSName("DisplayObject")
  @js.native
  trait DisplayObject extends js.Object {
    var x: Double = js.native
    var y: Double = js.native

    var angle: Double = js.native
    var alpha: Double = js.native

    var visible: Boolean = js.native
    val anchor: Point = js.native
  }

  @JSName("DisplayObjectContainer")
  @js.native
  trait DisplayObjectContainer extends DisplayObject
}
