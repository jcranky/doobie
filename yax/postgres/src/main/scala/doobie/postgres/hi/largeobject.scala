package doobie.postgres.hi

import doobie.imports._
import doobie.util.io.IOActions
import doobie.postgres.imports._

import java.io.{File, InputStream, OutputStream}

#+scalaz
import scalaz.syntax.apply._
#-scalaz
#+cats
import cats.implicits._
#-cats

object largeobject {

  lazy val io = new IOActions[LargeObjectIO]

  def copyFromFile(blockSize: Int, file: File): LargeObjectIO[Unit] =
    PFLO.getOutputStream.flatMap { os => io.copyFileToStream(blockSize, file, os) *> io.flush(os) }

  def copyToFile(blockSize: Int, file: File): LargeObjectIO[Unit] =
    PFLO.getInputStream.flatMap { is => io.copyStreamToFile(blockSize, file, is) }

  def copyFromStream(blockSize: Int, is: InputStream): LargeObjectIO[Unit] =
    PFLO.getOutputStream.flatMap { os =>
      io.copyStream(new Array[Byte](blockSize))(is, os)
    }

  def copyToStream(blockSize: Int, os: OutputStream): LargeObjectIO[Unit] =
    PFLO.getInputStream.flatMap { is =>
      io.copyStream(new Array[Byte](blockSize))(is, os)
    }
}
