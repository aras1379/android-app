package com.example.project_group27_sljg.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_group27_sljg.R
import io.garrit.android.multiplayer.ActionResult
import io.garrit.android.multiplayer.GameResult
import io.garrit.android.multiplayer.Player
import io.garrit.android.multiplayer.SupabaseCallback
import io.garrit.android.multiplayer.SupabaseService
import io.garrit.android.multiplayer.SupabaseService.currentGame
import io.garrit.android.multiplayer.SupabaseService.player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/*TODO: ROTATE SHIP,
   FIX EDGES: dont place over edges
   SET FINAL SHIP PLACEMENT
   MARK OUT: CELLS AT SHIP SIDES
   IF WRONG PLACEMENT -> DO AGAIN
   */

class GameViewModel() : ViewModel(), SupabaseCallback{

    val gridSize = 10
    val cellSize = 28.dp
    val boardState: MutableState<BoardState> = mutableStateOf(BoardState())

    data class Ship(
        val id: Int,
        val name: String,
        val length: Int,
        val imageResourceId: Int,
        var occupiedCells: List<Pair<Int, Int>> = emptyList(),
        var isHorizontal: MutableState<Boolean> = mutableStateOf(true),
        var isSet: MutableState<Boolean> = mutableStateOf(false),
        var isSunk: MutableState<Boolean> = mutableStateOf(false),
        var isHitted: MutableState<Boolean> = mutableStateOf(false),//tabort


        var isActiveShip: MutableState<Boolean> = mutableStateOf(false),
        var isClickedShip: Boolean = false, //ta bort
        var isShipPlaced: Boolean = false,
        var isUnmarked: Boolean = false, //tabort

        ){
    }
    object ShipData {
        val ships = listOf(
            Ship(0, "Ship4", 4, R.drawable.ship4),
            Ship(1, "Ship3", 3, R.drawable.ship31),
            Ship(2, "Ship21", 2, R.drawable.ship21),
            Ship(3, "Ship22", 2, R.drawable.ship22),
            Ship(4, "Ship11", 1, R.drawable.ship11),
            Ship(5, "Ship12", 1, R.drawable.ship12)
        )
    }

    data class GridCellType(
        //val ship: GameViewModel.Ship? = null,
        var isShip: MutableState<Boolean> = mutableStateOf(false),
        var isActive: MutableState<Boolean> = mutableStateOf(false),
        val isSelected: MutableState<Boolean> = mutableStateOf(false),
        val isWater: MutableState<Boolean> = mutableStateOf(false),
        var isEdge: MutableState<Boolean> = mutableStateOf(false),
        val isHit: MutableState<Boolean> = mutableStateOf(false),
        val shotCell: MutableState<Boolean> = mutableStateOf(false),
        val cellShot : MutableState<Boolean> = mutableStateOf(false),
        val isMiss: MutableState<Boolean> = mutableStateOf(false),
        var markedCell: MutableState<Boolean> = mutableStateOf(false),
        var edgeColor: MutableState<Color> = mutableStateOf(Color.Transparent), // ta bort

        var isShipId: Int? = null

    ) {
        /*TODO : all ships that "isShip/isSelected" => isActive when button pressed*/
        fun water() {
            if (!isWater.value && !isActive.value) {
                isWater.value = !isWater.value
            }
        }
        fun highlightShip() {
            isSelected.value = !isSelected.value
        }
        fun resetHighlightShip() {
            isSelected.value = false
            isWater.value = true
            isEdge.value = false
        }
        fun reset() {
            water()
            isWater.value = true
            isShipId = null
            isShip.value = !isShip.value
            isEdge.value = false
        }

        fun shipHit(){
            isHit.value = true
        }
        fun resetMarkedCell(){
        }
    }

    //val isCurrentPlayerReady = MutableStateFlow(false)
    val playerReady = MutableStateFlow(false)
    val isPlayClicked = mutableStateOf(false)
    val isGameEnd = mutableStateOf<Boolean>(false)

    data class PlayerLogic(
        val challPlayer : Player? = SupabaseService.currentGame?.player1,
        val opponPlayer: Player? = SupabaseService.currentGame?.player2,
        val isCurrPlayerReady: MutableStateFlow<Boolean> = MutableStateFlow(false),
        val isOppPlayerReady: MutableStateFlow<Boolean> = MutableStateFlow(false),
        val isPlayerTurn: MutableState<Boolean> = mutableStateOf(false),
        val arePlayersReady: MutableStateFlow<Boolean> = MutableStateFlow(false),
        val playerReady: MutableStateFlow<Boolean> = MutableStateFlow(false)
    ){
        fun setPlayerTurn(){
            isPlayerTurn.value = SupabaseService.currentGame?.player1 == player
        }
        fun switchTurn(){
            isPlayerTurn.value = !isPlayerTurn.value
            println("switchTurn, now turn : ${player}")
        }
    }

    /********************************
     * OTHER VARIABLES
     * ******************************/

    var isOpponentDone: MutableStateFlow<Boolean> = MutableStateFlow(false )

    val playerLogic = PlayerLogic()

    private val _coordinates = mutableStateListOf<GridCellType>()
    val coordinates: SnapshotStateList<GridCellType>
        get() = _coordinates
    val showPopUp = mutableStateOf(false)

    val gameResult = mutableStateOf<GameResult?>(null)

    val isGameFinished = mutableStateOf(false)

    private val _startRows = mutableStateMapOf<Int, Int>()

    val opponentBoardState: MutableState<BoardState> = mutableStateOf(BoardState())


    /********************************
     * INIT AND RESET
     * ******************************/
    init {
        SupabaseService.callbackHandler = this
        if(player?.id == SupabaseService.currentGame?.player1?.id){
            playerLogic.isPlayerTurn.value = true
           // isPlayerTurn.value = true
        }
        _coordinates.clear()

        for (i in 0 until gridSize * gridSize) {
            _coordinates.add(GridCellType())
        }
        playerLogic.isPlayerTurn.value = currentGame?.player1 == player
    }

    fun resetGame(){
        _coordinates.clear()
        boardState.value.cells.forEach { _ -> _coordinates.clear() }
        opponentBoardState.value.cells.forEach { _ -> _coordinates.clear() }

        for (i in 0 until gridSize * gridSize) {
            _coordinates.add(GridCellType())
        }

        gameResult.value = null
        isGameFinished.value = false
        updateStatusTxt.value = null
        _coordinates.forEach() { cell ->
            cell.resetHighlightShip()
            cell.isShip.value = false
            cell.isHit.value = false
            cell.isEdge.value = false
            cell.isActive.value = false
            cell.shotCell.value = false
            cell.isMiss.value = false
            cell.isSelected.value = false
            cell.isWater.value = true
            cell.isShipId = null
            cell.markedCell.value = false
        }

      //  arePlayersReady.value = false
        _selectedShip.value = null
        isPlayClicked.value = false
        isGameEnd.value = false
        isPlayShipSet.value = false
        //isOpponentPlReady.value = false
      //  isCurrentPlayerReady.value = false
        playerReady.value = false
        playerLogic.setPlayerTurn()
        allShipsSet.value = false
        showPopUp.value = false

        _ships.value.forEach { ship->
            ship.isSet.value = false
            ship.isSunk.value = false
            ship.occupiedCells.all { false }
            ship.isHitted.value = false
            ship.isActiveShip.value = false
            ship.isHorizontal.value = true
            ship.isClickedShip = false
            ship.isShipPlaced = false
            ship.isUnmarked = false
        }
    }

    data class BoardState(
        val cells: List<List<GridCellType>> = listOf()
    )



    /********************************
     * CELLS AND SELECTIONS
     *
     * ******************************/
    var selectedCellCoordinates = mutableStateOf<Pair<Int, Int>?>(null)

    var selectedCell = mutableStateOf<GridCellType?>(null)
        private set

    var isSelectedCellBool = mutableStateOf(false)

    fun selectCell( x: Int, y: Int){
        val cellIx = y*gridSize+x
      //  coordinates[cellIx].markedCell.value = true
        //coordinates[cellIx].previouslyMarked.value = true
        print("FROM Func selectCell: Selected cell:  x: $x y: ${y}")

        selectedCellCoordinates.value = Pair(x,y)
        coordinates[cellIx].shotCell.value = true
        coordinates[cellIx].markedCell.value = true
        isSelectedCellBool.value = true

    }

    /********************************
     * SHIPS
     * ******************************/
    private var _ships = mutableStateOf(ShipData.ships)
    val ships: State<List<Ship>> get() = _ships

    var selectedShipId = MutableLiveData<Int?>()

    private var _selectedShip = mutableStateOf<Ship?>(null)
    private val selectedShip: State<Ship?> get()= _selectedShip

    private val _allShipsSet = mutableStateOf(false)
    val allShipsSet = MutableStateFlow(false)

    fun isAllShipSet(): Boolean{
        return ships.value.all { ship->
            ship.isSet.value
        }
        allShipsSet.value = true
    }

    val isNoShipSet = mutableStateOf<Boolean>(false)
    fun isNoShipSet(): Boolean{
        return ships.value.all { ship->
            !ship.isSet.value
        }
        isNoShipSet.value
    }

    fun setSelectedShip(ship: Ship) {
        _selectedShip.value = ship
    }
    val isPlayShipSet = mutableStateOf(false)
    private fun setShip(shipId: Int){
        val ship = _ships.value.find{it.id == shipId}

        ship?.isSet?.value = true
        //_selectedShip.value?.isSet
        _ships.value.all{it.isSet.value}
        ship?.isUnmarked = true
    }

    fun isShipSetAt(row: Int, col: Int): Boolean{
        return ships.value.any{ ship ->
            isShipAtCoordinate(ship, row, col) && ship.isSet.value
        }
    }
    private fun isShipAtCoordinate(ship: Ship, row: Int, col: Int): Boolean{
        val step = if (ship.isHorizontal.value) 1 else gridSize
        return _coordinates.any{cell->
            cell.isShipId == ship.id && !isShipCollision( row, col, step)

            _coordinates.indexOf(cell) == row*gridSize + col
        }
    }
    //CHAT GPT for saving marked cells
    // https://chat.openai.com/share/b982704b-1c60-4caa-b164-03b1041edcfa
    fun resetAllCells() {
        for (x in 0 until gridSize) {
            for (y in 0 until gridSize) {
                val cellIx = y * gridSize + x
                coordinates[cellIx].markedCell.value = false
                coordinates[cellIx].shotCell.value = false
            }
        }

        isSelectedCellBool.value = false
        selectedCellCoordinates.value = null
    }

    // https://chat.openai.com/share/a7eeb0ff-8666-4980-94e4-777ac97b6c02
    fun placeShip(row: Int, col: Int) {
        println("go in placeship: row=$row, col=$col")
        val selectedShip = selectedShip.value ?: return

        val startIx = row * gridSize + col
        val step = if (selectedShip.isHorizontal.value) 1 else gridSize
        val endIx = startIx + (selectedShip.length - 1) * step

        if (endIx >= gridSize * gridSize || startIx < 0 || isShipCollision(startIx, endIx, step) || isOutsideBoard(row, col, shipLen = selectedShip.length, ship = selectedShip )){
            println("Invalid ship placement. Please try again. row=$row, col=$col")
            return
        }
        resetShipCells(selectedShip)

        if(!isShipCollision(startIx, endIx, step ) && !isOutsideBoard(row, col, shipLen=selectedShip.length, ship = selectedShip)) {
            val occupiedCells = mutableListOf<Pair<Int,Int>>()
            for (i in startIx..endIx step step) {
                val x = i % gridSize
                val y = i / gridSize
                _coordinates[i].apply {
                    isShip.value = true
                    highlightShip()
                    isShipId = selectedShip.id
                }
                occupiedCells.add(Pair(x,y))
                markEdges(i) // Mark edges around the ship
            }
            setShip(selectedShip.id)
            selectedShip.occupiedCells = occupiedCells
        }
        updateBoardState()
        _startRows[selectedShip.id] = startIx / gridSize
        isPlayShipSet.value = true
        playerLogic.playerReady.value = true
       // checkBothPlayersReady()
    }
    //have tried to get ships to not be able to be placed outside the board without any luck
    //so code below is mostly taken from ChatGPT
    //https://chat.openai.com/share/10fb063e-a3f6-43a6-9247-fe883b37fc1a
    private fun isOutsideBoard(row: Int, col: Int, shipLen: Int, ship: GameViewModel.Ship): Boolean {
        print("row: ${row}, col: ${col}")
        val endRow = 9
        val endCol = 9

        val shipEndCol = if(ship.isHorizontal.value) (endCol - shipLen) else endCol
        print("")
        val shipEndRow = if(ship.isHorizontal.value) endRow else (endRow - shipLen)

        if(row <= shipEndRow+1 && col <= shipEndCol+1)
            return false
        return true
    }

    private fun updateBoardState() {
        boardState.value = BoardState(_coordinates.chunked(gridSize))
    }

    private fun markEdges(index: Int) {
        val edgeIndices = listOf(-1, 1, -gridSize, gridSize)
        edgeIndices.forEach { adj ->
            val adjIndex = index + adj
            if (adjIndex in _coordinates.indices && !_coordinates[adjIndex].isShip.value) {
                _coordinates[adjIndex].apply {
                    isEdge.value = true
                    edgeColor.value = Color.Gray
                }
            }
        }
    }

    //CHATGPT: Ive tried many solutions but cannot find whats
    //not working. So code below is mostly cited
    // https://chat.openai.com/share/aca12990-0ee5-4a12-8c0a-4d3f7545df8d
    private fun resetShipCells(selectedShip: Ship) {
        _coordinates.forEach { cell ->
            if (cell.isShipId == selectedShip.id) {
                cell.reset()
                cell.isShip.value = false
                cell.isEdge.value = false
                cell.edgeColor.value = Color.Transparent
                cell.isShipId = null
                resetEdges(cell) // Reset adjacent edges
            }
        }
    }

    private fun resetEdges(cell: GridCellType) {
        val index = _coordinates.indexOf(cell)
        val adjacentIndices = listOf(-1, 1, -gridSize, gridSize)
        adjacentIndices.forEach { adj ->
            val adjIndex = index + adj
            if (adjIndex in _coordinates.indices && !_coordinates[adjIndex].isShip.value) {
                _coordinates[adjIndex].apply {
                    isEdge.value = false
                    edgeColor.value = Color.Transparent
                }
            }
        }
    }

    private fun isShipCollision(startIx: Int, endIx: Int, step: Int): Boolean {
        for (i in startIx..endIx step step) {
            if (_coordinates[i].isShip.value || hasAdjacentShipOrEdge(i)) {
                return true
            }
        }
        return false
    }

    private fun hasAdjacentShipOrEdge(index: Int): Boolean {
        val adjacentIndices = listOf(-1, 1, -gridSize, gridSize)
        return adjacentIndices.any { adj ->
            val adjIndex = index + adj
            adjIndex in _coordinates.indices && (_coordinates[adjIndex].isShip.value || _coordinates[adjIndex].isEdge.value)
        }
    }

    fun rotateShip() {
        val selectedShip = selectedShip.value ?: return
        selectedShip.isHorizontal.value = !selectedShip.isHorizontal.value
    }

    /**********************************************
     * ACTION HANDLERS
     * *******************************************/
    private fun shotReceived(){
        showPopUp.value = true
    }
    private suspend fun leaveGame(){
        //SupabaseService.gameFinish()
        SupabaseService.leaveGame()
    }
    private fun checkWinCondition(): Boolean{

        println("go into checkWinCondition ")
        return ships.value.all{ship->
            ship.occupiedCells.all { (x,y) ->
                val cIx = y*gridSize+x
                coordinates[cIx].isHit.value
            }
        }

    }
    fun buttonShotClick(){
        println("go into button click ")
        var playerNow = ""

        if( playerLogic.isPlayerTurn.value ){
            playerNow = player?.name.toString()
            println("player turn buttonShotClick : $playerNow")


            val x = selectedCellCoordinates.value?.first
            val y = selectedCellCoordinates.value?.second

            if (x != null && y!=null) {
                selectCell(x,y)
                val cellIx = y*gridSize +x
                _coordinates[cellIx].resetMarkedCell()
                _coordinates[cellIx].markedCell.value = false
                _coordinates[cellIx].shotCell.value = true

            }

            viewModelScope.launch {
                //ändrat här
                selectedCellCoordinates.value?.first?.let { selectedCellCoordinates.value?.second?.let { it1 ->
                    sendTurn(it,
                        it1
                    )
                } }

            }

        }

    }


    val updateStatusTxt = mutableStateOf<ActionResult?>(null)

    fun sendStatusMessage(text: String): String {
        println({text})
        shotReceived()
        showPopUp.value = true
        return text
    }

    /**********************************************
     * GAME LOGIC BELOW
     * *******************************************/



    fun playerReady() {
        println("go into player ready ")
        viewModelScope.launch {
            SupabaseService.playerReady()
            isOpponentDone.value

        }

        playerLogic.playerReady.value = true



        if(playerLogic.isPlayerTurn.value ){
            println("TURN (playerLogic) ${player?.name}")
        }

    }

    private fun releaseTurn(){
        println("go into release turn ")
        viewModelScope.launch {
            playerLogic.isPlayerTurn.value = false
            SupabaseService.releaseTurn()
            isOpponentDone.value = true

        }
    }
    //https://proandroiddev.com/kotlins-vararg-and-spread-operator-4200c07d65e1
    private fun sendTurn(vararg values: Int) {
        println("Go in to sendTurn function")
        if(playerLogic.isPlayerTurn.value){
            println("TURN (playerLogic) in SendTurn: ${player?.name}")
        }
        val x = selectedCellCoordinates.value?.first
        val y = selectedCellCoordinates.value?.second
        println("selected cells SendTurn : x:  $x, y : $y")
        viewModelScope.launch {
            if (y != null && x!= null) {
                SupabaseService.sendTurn(x ,y )

            }
        }
    }

    private fun sendAnswer(result: ActionResult) {
        println("go into sendAnswer")
        viewModelScope.launch {
            SupabaseService.sendAnswer(result)
        }
    }

    private fun gameFinish(status: GameResult) {
        println("go into gamefinish status $status")
        viewModelScope.launch {
            SupabaseService.gameFinish(status)
            println("go into gamefinish status $status")
            gameResult.value = status
            isGameFinished.value = true
            SupabaseService.leaveGame()
        }
    }

    override suspend fun playerReadyHandler() {
        println("go into playerreadyHANDLER ")
        /*TODO("SKRIV UT OM VILL, KALLA ANDRA FUNK + KOLLA VEMS TUR ")*/
        val coordinates = selectedCellCoordinates.value ?: return
        isOpponentDone.value = true

    }

    override suspend fun releaseTurnHandler() {
        println("go into releaseturn HANDLER ")
        playerLogic.isPlayerTurn.value = true
    }

    private fun isShipSunk(shipId: Int?): Boolean{
        if (shipId == null) return false
        val ship = ships.value.find { it.id == shipId } ?: return false
        return ship.occupiedCells.all { (x, y) ->
            val cellIndex = y * gridSize + x
            coordinates[cellIndex].isHit.value
        }
    }


    override suspend fun actionHandler(x: Int, y: Int) {
        println("go into actionHANDLER ")
        println("actionHandler cells: x: $x, y: $y")


        val cellIndex = y * gridSize + x
        val cell = coordinates[cellIndex]

        if (cell.isShip.value) {
            cell.shipHit()
            //cellHit(x,y)
            val shipId = cell.isShipId
         //   val isSunk = shipId?.let{isShipSunk(it)} ?: false

            if (shipId!=null) {
               val isSunkShip = isShipSunk(shipId)

                if (isSunkShip) {
                    updateShipState(shipId, true)
                    updateStatusTxt.value = ActionResult.SUNK
                    // cell.isHit.value = true
                    sendAnswer(ActionResult.SUNK)
                }else{
                    updateStatusTxt.value = ActionResult.HIT
                    sendAnswer(ActionResult.HIT)
                }
            }
            if (checkWinCondition()) {
                println("checkForWin / checkWinCondition TRUE ")

                println("Current player wins")
                gameFinish(GameResult.LOSE)
            }
        } else {
            updateStatusTxt.value = ActionResult.MISS
            cell.shotCell.value = true
            sendAnswer(ActionResult.MISS)
            playerLogic.switchTurn()
            if(playerLogic.isPlayerTurn.value ){
                println("turn switchturn actionhndlr : ${player?.name}")
            }
        }
    }
    private fun updateShipState(shipId: Int, isSunk: Boolean) {
        _ships.value = _ships.value.map { ship ->
            if (ship.id == shipId) {
                ship.apply { this.isSunk.value = isSunk }
            } else {
                ship
            }
        }
    }

    override suspend fun answerHandler(status: ActionResult) {
        println("go into answerHANDLER ")
        println(status)
        if(playerLogic.isPlayerTurn.value ){
            println("turn: ${player?.name}")
        }
        /* TODO("Not yet implemented") KAN ANVÄNDA if(status = ActionResult.HIT etc*/
        // answerHandler(status)
        val x = selectedCellCoordinates.value?.first
        val y = selectedCellCoordinates.value?.second
        if(status == ActionResult.HIT){

            println("CELL HITTED")
            sendStatusMessage("HIT")

            if (x != null && y!=null) {
                val cellIx = y*gridSize +x
                _coordinates[cellIx].isHit.value = true
            }
        }else if(status == ActionResult.SUNK){
            selectedShip.value?.isSunk
            if (y != null && x!=null) {
                _coordinates[y*gridSize+x].shotCell.value
            }

            sendStatusMessage("SUNK")
            if(checkWinCondition()){
                println("current player win")
                gameFinish(GameResult.WIN)
                leaveGame()
            }

        }else{
            sendStatusMessage("MISS")
            playerLogic.isPlayerTurn.value = false
            //playerLogic.switchTurn()
            if (x != null && y!=null) {
                val cellIx = y*gridSize +x
                _coordinates[cellIx].markedCell.value = true
                _coordinates[cellIx].isWater.value = true
            }
            if(playerLogic.isPlayerTurn.value  ){
                println("turn before releaseturn: ${player?.name}")
            }
            releaseTurn()
        }
        updateStatusTxt.value = status
    }

    override suspend fun finishHandler(status: GameResult) {
        println("Go into Finish Handler status: $status")
        isGameFinished.value = true
        SupabaseService.leaveGame()
        if(isGameFinished.value){
        }
    }


}


