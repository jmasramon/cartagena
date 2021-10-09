(ns cartagena.data-abstractions.game
  (:require
   [clojure.data.generators :refer [rand-nth shuffle]]
   [cartagena.core :refer [def-num-players num-cards]]
   [cartagena.data-abstractions.player :refer [random-players color colors update-player-in-players add-random-card-to-player decrease-actions actions reset-actions cards set-cards]]
   [cartagena.data-abstractions.deck :refer [random-deck remove-card]]
   [cartagena.data-abstractions.board :as b :refer [make-board boat]]))

;; Functions that need to know how game is implemented
;; game-state: 
;; {players turn board deck}
(defn random-initial-turn
  "Choose a new random color out of the players colors"
  [turns-reservoir]
  (rand-nth (seq turns-reservoir)))

(defn players-turns
  "Define the random order in which the players will play"
  [players-colors]
  (let [random-ordered-players-colors (shuffle players-colors)]
    (zipmap random-ordered-players-colors (rest (cycle random-ordered-players-colors)))))

(defn make-game
  "Create a new game"
  ([]
   (let [players (random-players def-num-players)
         players-colors (colors players)]
     {:players players
      :turn-order (players-turns players-colors)
      :turn (random-initial-turn players-colors)
      :deck (random-deck)
      :board (make-board players)}))
  ([num-players]
   (let [players (random-players num-players)
         players-colors (colors players)]
     {:players players
      :turn-order (players-turns players-colors)
      :turn (random-initial-turn players-colors)
      :deck (random-deck)
      :board (make-board players)})))

;; getters
(defn players [game]
  (game :players))

(defn turn-order [game]
  (game :turn-order))

(defn turn [game]
  (game :turn))

(def cur-player turn) ;; just an alias

(defn next-turn [game]
  ((turn game) (turn-order game)))

(defn deck [game]
  (game :deck))

(defn board [game]
  (game :board))

(defn active-player [game]
  (let [turn (turn game)
        players (players game)]
    (first (filter #(= turn (first (keys %))) players))))

(defn active-player-color [game]
  (turn game))

(defn next-player [game]
  (next-turn game))

(defn winner? [game]
  (let [board (board game)
        boat (boat board)
        pieces (vals (get-in boat [:pieces]))]
    (> (count (filter #(= % num-cards) pieces)) 0)))

;; setters
(defn set-players [game players]
  (assoc-in game [:players] players))

(defn set-turn-order [game turn-order]
  (assoc-in game [:turn-order] turn-order))

(defn set-turn [game turn]
  (assoc-in game [:turn] turn))

(defn set-board [game board]
  (assoc-in game [:board] board))

(defn set-deck [game deck]
  (assoc-in game [:deck] deck))

;; state-changers
(defn add-random-card-to-active-player
  "Add a (random) card to the active player"
  [game]
  (let [color (active-player-color game)
        players (players game)
        updated-players (add-random-card-to-player players color)]
    (set-players game updated-players)))

(defn move-piece 
  "Moves a piece from to"
  [game from to]
  (let [board (board game)
        color (active-player-color game)]
    (set-board game (b/move-piece board from to color))))

(defn turn-played 
  "Reduces the num of actions from the active user.
   If zero remaining, set 3 actions to next user"
  [game]
  (let [color (color (active-player game))
        next-player (next-player game)
      	;; TODO: next four lines require its own abstraction new-players-state-after-turn
        decreased-action-players (decrease-actions (players game) color)
        remaining-actions (actions decreased-action-players color)
        new-players (if (= 0 remaining-actions)
                     (reset-actions decreased-action-players next-player)
                     decreased-action-players)
        new-game (set-players game new-players)]
    (if (= 0 remaining-actions)
      (set-turn new-game next-player)
      new-game)))

(defn remove-played-card
  "Removes card played from active user"
  [game card]
  (let [players (players game)
        active-player (active-player game)
        color (color active-player)
        cards (cards active-player)
        new-cards (remove-card cards card)
        updated-player (set-cards active-player new-cards)
        updated-players (update-player-in-players players color updated-player)]
    (set-players game updated-players)))


