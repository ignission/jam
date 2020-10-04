package jam.websocket.interpreters

import monix.eval.Task

import jam.application.dsl.Result.Result
import jam.application.dsl.{Result, StoreDSL}
import jam.websocket.{AppError, NotFound}

import scala.collection.immutable.HashMap
import scala.reflect.ClassTag

class InMemoryStoreInterpreter[Key, A](implicit classTag: ClassTag[A])
    extends StoreDSL[Task, Key, AppError, A] {
  private var store = HashMap.empty[Key, A]

  type TaskResult[B] = Result[Task, AppError, B]

  def getAll: TaskResult[Seq[A]] =
    Result.success[Task, AppError, Seq[A]](store.toSeq.map(_._2))

  def get(key: Key): TaskResult[A] =
    if (store.contains(key)) Result.success[Task, AppError, A](store(key))
    else Result.error[Task, AppError, A](NotFound[Key, A](key))

  def put(key: Key, obj: A): TaskResult[A] =
    Task {
      if (store.contains(key))
        store = store.updated(key, obj)
      else
        store = store + (key -> obj)

      Right(obj)
    }

  def delete(key: Key): Result.Result[Task, AppError, Unit] =
    Task {
      store = store - key
      Right(())
    }
}
