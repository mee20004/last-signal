class Room(
    val name: String,
    val description: String,
    val exits: Map<String, String>,
    val items: MutableList<String> = mutableListOf()
)

class Player {
    var location = "quarters"
    val inventory = mutableListOf<String>()
}

class Game {
    private val player = Player()
    private var engineeringUnlocked = false
    private var podPowered = false
    private var signalSent = false
    private var running = true
    private val rooms = mapOf(
        "quarters" to Room(
            "Crew Quarters",
            "You wake to a warning alarm. The station is empty. A keycard lies on your bunk.",
            mapOf("corridor" to "corridor"),
            mutableListOf("keycard")
        ),
        "corridor" to Room(
            "Central Corridor",
            "Red lights lead toward an escape pod. Engineering is behind a locked door. " +
                "A flight recorder could provide the coordinates needed for a rescue signal.",
            mapOf("quarters" to "quarters", "engineering" to "engineering", "pod" to "pod"),
            mutableListOf("flight recorder")
        ),
        "engineering" to Room(
            "Engineering",
            "The station's main reactor is dead. A spare power cell sits beside the console.",
            mapOf("corridor" to "corridor"),
            mutableListOf("power cell")
        ),
        "pod" to Room(
            "Escape Pod",
            "The pod has no power. Its emergency transmitter has a slot for a flight recorder.",
            mapOf("corridor" to "corridor")
        )
    )

    fun run() {
        println("LAST SIGNAL")
        println("Find a way off the damaged station. Your choices decide how the story ends.")
        look()
        // Keep taking actions until the player launches or quits.
        while (running) {
            println("\n1. Look around")
            println("2. Move")
            println("3. Pick up an item")
            println("4. Check inventory")
            println("5. Use an item")
            println("6. Launch escape pod")
            println("7. Quit")
            val choice = ask("Choose an action: ") ?: break
            when (choice) {
                "1" -> look()
                "2" -> move()
                "3" -> takeItem()
                "4" -> showInventory()
                "5" -> useItem()
                "6" -> launch()
                "7" -> running = false
                else -> println("Choose a number from 1 to 7.")
            }
        }
        println("Thanks for playing Last Signal.")
    }

    private fun ask(prompt: String): String? {
        print(prompt)
        val answer = readlnOrNull()
        if (answer == null) {
            running = false
            return null
        }
        return answer.trim().lowercase()
    }

    private fun currentRoom(): Room = rooms.getValue(player.location)

    private fun look() {
        val room = currentRoom()
        println("\n${room.name}")
        println(room.description)
        if (room.items.isNotEmpty()) {
            println("Items here: ${room.items.joinToString()}")
        } else {
            println("There are no items left to pick up here.")
        }
        println("Exits: ${room.exits.keys.joinToString()}")
        if (engineeringUnlocked && player.location == "corridor") {
            println("The engineering door is unlocked.")
        }
        if (player.location == "pod") {
            println("Pod power: ${if (podPowered) "online" else "offline"}")
            println("Rescue signal: ${if (signalSent) "sent" else "not sent"}")
        }
    }

    private fun move() {
        val room = currentRoom()
        println("Go to: ${room.exits.keys.joinToString()}")
        val destination = ask("Enter a destination: ") ?: return
        val nextRoom = room.exits[destination]
        if (nextRoom == null) {
            println("There is no exit with that name here.")
            return
        }
        if (nextRoom == "engineering" && !engineeringUnlocked) {
            println("The engineering door is locked. Use a keycard in the corridor.")
            return
        }
        player.location = nextRoom
        look()
    }

    private fun takeItem() {
        val room = currentRoom()
        if (room.items.isEmpty()) {
            println("There is nothing to pick up here.")
            return
        }
        println("Items: ${room.items.joinToString()}")
        val item = ask("Pick up which item? ") ?: return
        if (!room.items.contains(item)) {
            println("That item is not here.")
            return
        }
        // Move the item from the room into the player's inventory.
        room.items.remove(item)
        player.inventory.add(item)
        println("Picked up: $item.")
    }

    private fun showInventory() {
        if (player.inventory.isEmpty()) {
            println("Your inventory is empty.")
            return
        }
        println("Inventory (${player.inventory.size} items): ${player.inventory.joinToString()}")
    }

    private fun useItem() {
        if (player.inventory.isEmpty()) {
            println("Your inventory is empty.")
            return
        }
        showInventory()
        val item = ask("Use which item? ") ?: return
        if (item !in player.inventory) {
            println("You are not carrying that item.")
            return
        }
        when (item) {
            "keycard" -> unlockEngineering()
            "power cell" -> powerPod()
            "flight recorder" -> sendSignal()
        }
    }

    private fun unlockEngineering() {
        if (player.location != "corridor") {
            println("Use the keycard at the engineering door in the corridor.")
            return
        }
        engineeringUnlocked = true
        player.inventory.remove("keycard")
        println("The door unlocks. The damaged reader keeps the keycard.")
    }

    private fun powerPod() {
        if (player.location != "pod") {
            println("The power cell fits the escape pod. Try it there.")
            return
        }
        podPowered = true
        player.inventory.remove("power cell")
        println("You install the power cell. The pod and transmitter come online.")
    }

    private fun sendSignal() {
        if (player.location != "pod") {
            println("The flight recorder fits the transmitter inside the escape pod.")
            return
        }
        if (!podPowered) {
            println("The transmitter needs power first.")
            return
        }
        signalSent = true
        player.inventory.remove("flight recorder")
        println("The recorder sends your coordinates. A rescue ship answers your signal.")
    }

    private fun launch() {
        if (player.location != "pod") {
            println("You must be inside the escape pod to launch.")
            return
        }
        if (!podPowered) {
            println("The pod cannot launch without a power cell.")
            return
        }
        val answer = ask("Leave the station now? (y/n): ") ?: return
        if (answer != "y") {
            println("You stay aboard for now.")
            return
        }
        // The optional rescue signal changes the ending.
        if (signalSent) {
            println("\nENDING: A SIGNAL ANSWERED")
            println("Your pod leaves the station. The rescue ship follows your signal and brings you home.")
        } else {
            println("\nENDING: INTO THE DARK")
            println("You escape, but no one knows where you are. Your pod drifts toward an uncertain future.")
        }
        running = false
    }
}

fun main() {
    Game().run()
}
