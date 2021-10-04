(ns cartagena.gameplay
  (:require
   [cartagena.core :refer [card-types]]
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
;   The board itself is made up of six double-sided sections, each of which has a different permutation of the same six pictures: 
;   daggers, pirate hats, pistols, bottles of rum, skulls, and skeleton keys. These six sections can be combined in any order
;   each player has a group of six pirates and the objective is to have all six escape through the tortuous underground passage that connects the fortress to the port
;   Each player is dealt six cards out of a set of 102: 17 cards for each of the six pictures
;		three moves every turn
;   During one's turn, a player can make between one and three moves. Each move sees one of the player's pirate going either forward or backward. 
;   Pirates are moved forward by playing the cards in their hand. New cards can only be obtained by moving backward.
;		If cards available, move forward to first free board square with same symbol as played card. 
;   If there are no unoccupied spaces ahead, the pirate advances directly to the sloop
;   To move backward, the player selects a pirate and moves it back to the closest square with one or two pirates. Earns as many cards as occupants. Loses move. 
;		Pass loses a move

;; Abstraction barriers: 
;;      There is a game. It contains a state. State contains: players, deck, board, active-player. Players contains: color, cards, moves. 
;;      Deck contains: cards. Board contains: squares. Squares contain: type, pieces
;;      -- GAME
;;        -- game (change the GAME): start-game, end-game --
;; 	    -- STATE
;;        -- game status: winner?, next-round --
;;      -- interact with user: update-board, show-cards, ask-for-action (choose-card or pass), show-result  --
;;        UP:
;;        -- moves (change the STATE): reset, play-card, pass --
;;        DOWN:
;;        -- get, set the board-state
;;        -- get, set the users-state
;;        -- get, set the deck-state
;;          -- BOARD
;;            -- manage board: update-card-placement
;;            -- SQUARES
;;              -- manage square: add-piece, remove-piece, full?
;;        -- PLAYERS
;;          -- manage players-state: update-active-player, update-player-actions, update-player-cards,
;;        -- DECK
;;          -- manage deck-state: get-card, empty? 


; treats deck as a stack: returns first card and a new
; diminished deck
(defn draw-one-from [deck]
  (let [[card & newDeck] deck]
    [card newDeck]))

;; TODO: change param order so a partial application can generate ex: draw-3-from
(defn draw [deck n]
  (loop [deck deck n n acc []]
    (if (= n 0)
      [(reverse acc) deck]
      (let [[card newDeck] (draw-one-from deck)]
        (recur newDeck (dec n) (cons card acc))))))
	; (for [i (take n (range) newDeck deck)
	; 	:let [[card newDeck] (draw-one-from newDeck)]]
	; 	card))

; (defn pass [game-state user]
; 	(let [newPlayers (decrease-turns (players game-state) user)]
; 		(set-players game-state newPlayers)))

(defn pass [game-state]
  "loose a turn"
  (let [current-player (player-color (active-player game-state))
        next-player ((turn game-state) (turn-order game-state))
      	;; TODO: next three lines require its own abstraction new-players-state-after-turn
        updatedPlayers (decrease-turns (players game-state) current-player)
        remainingActions (actions updatedPlayers current-player)
        newPlayers (if (= 0 remainingActions)
                     (add-turns updatedPlayers next-player)
                     updatedPlayers)
        updatedGameState (set-players game-state newPlayers)
        newGameState (if (= 0 remainingActions)
                       (set-turn updatedGameState next-player)
                       updatedGameState)]
		; (println "newGameState" newGameState)
    newGameState))

; (defn playCard [game-state user card origin]
; 	(let [dest (find-empty-slot game-state card origin)])
; 		(move-player orig dest user))

(defn fall-back [game-state square-index]
  "Goes back to the first non empty slot"
  (let [board (board game-state)
        players (players game-state)
        activePlayer (active-player game-state)
        current-player (player-color activePlayer)
        cards (player-cards activePlayer)
        newCards (add-card cards (random-card card-types))
        newPlayer (set-cards activePlayer newCards)
        newPlayers (update-player-in-players players current-player newPlayer)
        fallBackSlot (find-nonempty-slot game-state square-index)]
    (pass
     (set-players
      (set-board game-state
                 (move-piece board square-index fallBackSlot current-player)) newPlayers))))

(defn play-card [game-state card current-position]
  "getActiveUser
	moveCard (from board current-position to next available space -empty slot same type-)
	pass"
  (let [board (board game-state)
        players (players game-state)
        activePlayer (active-player game-state)
        current-player (player-color activePlayer)
        cards (player-cards activePlayer)
        newCards (remove-card cards card)
        newPlayer (set-cards activePlayer newCards)
        newPlayers (update-player-in-players players current-player newPlayer)
        destination (find-empty-slot game-state card current-position)]
    (pass
     (set-players
      (set-board game-state
                 (move-piece board current-position destination current-player)) newPlayers))))

(defn play-a-turn [game-state user action])

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


