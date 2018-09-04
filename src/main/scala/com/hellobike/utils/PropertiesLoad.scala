package com.hellobike.utils

import java.io.{FileNotFoundException, IOException}
import java.util.Properties

import com.hellobike.constants.Constants

object PropertiesLoad {
  val properties = new Properties()

  def loadFilePath(): Unit = {
    try {
      properties.load(PropertiesLoad.getClass.getClassLoader.getResourceAsStream("hellobike.properties"))
    } catch {
      case ex: FileNotFoundException => ex.printStackTrace()
      case ex: IOException => ex.printStackTrace()
    }
  }

  def getString(key:String):String={
    properties.getProperty(key)
  }

  def getInt(key:String):Int={
    properties.getProperty(key).toInt
  }

  def main(args: Array[String]): Unit = {
    println(Constants.ES_NDOES)
    loadFilePath()
    println(PropertiesLoad.getString(Constants.ES_NDOES))
  }

}