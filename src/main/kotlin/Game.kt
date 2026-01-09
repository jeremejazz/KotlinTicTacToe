package me.jereme

import kotlin.system.exitProcess

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
            checkTheBoard()
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

    private fun checkTheBoard() {
        val winningCombinations = listOf(
            listOf(1,1,2),
            listOf(3,4,5),
            listOf(6,7,8),
            listOf(0,3,6),
            listOf(1,4,7),
            listOf(2,5,8),
            listOf(0,4,8),
            listOf(2,4,6),
            )

        val player1Cells = mutableListOf<Int>()
        val player2Cells = mutableListOf<Int>()

        board.forEachIndexed { index, cell ->
            if(cell.placeholder == 'X'){
                player1Cells.add(element = index)
            }
            if(cell.placeholder == 'O'){
                player2Cells.add(element = index)
            }
        }
//        println("YOUR MOVES: $player1Cells")
//        println("COMPUTER MOVES: $player2Cells")
        run CombinationLoop@{
            winningCombinations.forEach { combination ->
                if(player1Cells.containsAll(elements=combination)){
                    won()
                    return@CombinationLoop
                }
                if(player2Cells.containsAll(elements = combination)){
                    lost()
                    return@CombinationLoop
                }
            }
        }

        if(board.none{ it is Cell.Empty} && status is Status.Running){
            draw()
        }

        if(status is Status.GameOver){
            finish()
            playAgain()
        }

    }

    private fun finish(){
        status = Status.Idle
        board.replaceAll{ Cell.Empty }
    }



    private fun won() {
        status = Status.GameOver
        printBoard()
        println()
        println("Congratulations ${player.name}, You WON!")
        println()
    }

    private fun lost() {
        status = Status.GameOver
        printBoard()
        println("Sorry ${player.name}, You LOST!")
    }

    private fun draw() {
        status = Status.GameOver
        printBoard()
        println("DRAW!")
    }

    private fun playAgain() {
        print("Do you wish to play another one? Y/N: ")
        val input = readlnOrNull()

        try {
            require(input != null)
            val capitalizedInput = input.replaceFirstChar(Char::titlecase)
            val positive = capitalizedInput.contains('Y')
            val negative = capitalizedInput.contains('N')
            require(positive || negative)
            if(positive) {
                start()
            }
            else if(negative) {
                exitProcess(0)
            }
        }catch (e: Throwable){
            println("Wrong Option.")
            playAgain()
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




