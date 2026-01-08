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
        while(status is Status.Running){
              getCell()
        }

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

    private fun getCell(){

        print("Enter a number (1-9): ")
        val input = readlnOrNull()

        try{
            require(value = input != null)
            val cellNumber = input.toInt() - 1
            require(value = cellNumber in 0 .. 8)
            setCell(cellNumber)
        } catch (e: Throwable){
            println("Invalid Number")
        }

    }

    private fun setCell(cellNumber: Int): Unit {
        val cell = board[cellNumber]
        if (cell is Cell.Empty){
            board.set(index = cellNumber,
                element = Cell.Filled(player=player)
                )
            generateComputerMove()
            printBoard()
        }else{
            println("Cell Taken, Choose Another.")
        }
    }

    private fun generateComputerMove(): Unit {
        try {
            val availableCells = mutableListOf<Int>()
            board.forEachIndexed { index, cell ->
                if (cell is Cell.Empty) availableCells.add(element=index)
            }
            if(availableCells.isNotEmpty()){
                val randomCell = availableCells.random()
                board.set(
                    index=randomCell,
                    element = Cell.Filled(player=Player())
                )
            }
        }catch(e: Throwable){
            println("Error: ${e.message}")
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




