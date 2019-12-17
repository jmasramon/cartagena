(ns cartagena.gameplay
	(:require 	[cartagena.core :refer [card-types]]
				[cartagena.generators :refer [initial-state random-card]]
				[cartagena.queries :refer [winner?]]
				[cartagena.getters.mainEntities :refer [board players turn turn-order]]
				[cartagena.getters.sdariEntities :refer [actions]]
				[cartagena.getters.semanticEntities :refer [active-player]]
				[cartagena.getters.otherEntities :refer [player-color player-cards]]
				[cartagena.setters.mainEntities :refer [set-players set-board set-turn]]
				[cartagena.setters.sdariEntities :refer [update-player-in-players]]
				[cartagena.setters.otherEntities :refer [set-cards]]
				[cartagena.setters.semanticEntities :refer [decrease-turns remove-card add-card add-turns]]
				[cartagena.moves :refer [move-piece find-empty-slot find-nonempty-slot]]))

; Top level functions: get a state and create the next one

; Rules:
;		three moves every turn
;		if cards available, move forward to a free board square with same symbol as played card. Loses card and move
;       move backwards to closest occupied (1 or 2) board square. Earns as many cards as occupants. Loses move
;		pass loses a move


; treats deck as a stack: returns first card and a new
; diminished deck
(defn draw-one [deck]
	(let [[card & newDeck] deck ]
		[card newDeck]))

(defn draw [deck n]
	(loop [deck deck n n acc []]
		(if (= n 0)
			[(reverse acc) deck]
			(let [[card newDeck] (draw-one deck)]
			 	(recur newDeck (dec n) (cons card acc)))
		)
	))
	; (for [i (take n (range) newDeck deck)
	; 	:let [[card newDeck] (draw-one newDeck)]]
	; 	card))

; (defn pass [game-state user]
; 	(let [newPlayers (decrease-turns (players game-state) user)]
; 		(set-players game-state newPlayers)))

(defn pass [game-state]
	"loose a turn"
	(let [
		color (player-color (active-player game-state))
		newColor ((turn game-state) (turn-order game-state))
		updatedPlayers (decrease-turns (players game-state) color)
		remainingActions (actions updatedPlayers color)
		newPlayers (if (= 0 remainingActions)
					(add-turns updatedPlayers newColor)
					updatedPlayers)
		updatedGameState (set-players game-state newPlayers)
		newGameState (if (= 0 remainingActions)
						(set-turn updatedGameState newColor)
						updatedGameState)]
		; (println "newGameState" newGameState)
		newGameState
		))

; (defn playCard [game-state user card origin]
; 	(let [dest (find-empty-slot game-state card origin)])
; 		(move-player orig dest user))

(defn fall-back [game-state square-index]
	"Goes back to the first non empty slot"
	(let [
		board (board game-state)
		players (players game-state)
		activePlayer (active-player game-state)
		color (player-color activePlayer)
		cards (player-cards activePlayer)
		newCards (add-card cards (random-card card-types))
		newPlayer (set-cards activePlayer newCards)
		newPlayers (update-player-in-players players color newPlayer)
		fallBackSlot (find-nonempty-slot game-state square-index)]
		(pass
			(set-players
				(set-board game-state
						(move-piece board square-index fallBackSlot color)) newPlayers))
		))

(defn play-card [game-state card square-index]
	"getActiveUser
	moveCard (from board square-index to next available space -empty slot same type-)
	pass"
	(let [
		board (board game-state)
		players (players game-state)
		activePlayer (active-player game-state)
		color (player-color activePlayer)
		cards (player-cards activePlayer)
		newCards (remove-card cards card)
		newPlayer (set-cards activePlayer newCards)
		newPlayers (update-player-in-players players color newPlayer)
		emptySlot (find-empty-slot game-state card square-index)
		]
		(pass
			(set-players
				(set-board game-state
						(move-piece board square-index emptySlot color)) newPlayers))
	))

(defn play-a-turn [game-state user action]
	)

(defn play-next-turn [game-state]
	; print user to play
	; while user has actions
		; print user info
		; ask for user action
		; apply action
		;(play-a-turn game-state user action)
	)

(defn play []
	(let [game-state (initial-state)]
		(loop [game-state game-state]
			(if (winner? game-state)
				(print "And the winner is")
				(recur (play-next-turn game-state))))))


