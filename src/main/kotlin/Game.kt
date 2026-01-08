package me.jereme

class Game {
    private val board = MutableList<Cell>(size=9){ Cell.Empty }
    private var status: Status= Status.Idle
    private lateinit var player: Player

    fun start() {
        status = Status.Running
        println(" ------------------------- ")
        println("| Welcome to TIC-TAC-TOE |")
        println("| Pick a number from 1-9 |")
        println(" ------------------------ ")

        getName()
    }

    private fun getName(){
        print("Choose Your Name: ")
        val name = readlnOrNull()
        try{
            require(value = name != null)
            player = Player(name = name, symbol = 'X')
            println("It's your move, $name")
            printBoard()
        }catch(e: Throwable){
            println("Invalid name.")
        }
    }

    private fun printBoard(){

        println("     ************")
        board.forEachIndexed{ i, item ->
            if(i % 3 == 0){

                print("     | ")
            }
            print("${item.placeholder}  ")

            if((i+1) % 3 == 0 ){
                println("| ")
            }
        }
        println("     ************")

    }
}

data class Player(
    val name: String = "Computer",
    val symbol: Char = 'O'
    )


sealed class Status{
    object Idle: Status()
    object Running: Status()
    object GameOver: Status()
}

sealed class Cell(val placeholder: Char){
    object Empty: Cell(placeholder = '_')
    data class Filled(val player: Player): Cell(placeholder = player.symbol)
}




