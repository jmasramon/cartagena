(ns cartagena.turnSequencing
	(:require [cartagena.getters.sdariEntities :refer [player]]
                [cartagena.getters.mainEntities :refer [players]]
				[cartagena.setters.sdariEntities :refer [update-player-in-players]]))

; TODO: some of the functions need to be omved to a new getters and some to a new setters


; give the active user three actions to play with
(defn start-turn [game-state turn-color]
	(let [players (players game-state)
		  player (player players turn-color)
		  modified-player (assoc-in player [turn-color :actions] 3)
		  modified-players (update-player-in-players players turn-color modified-player) ]
	(assoc-in game-state [:players] modified-players)))

