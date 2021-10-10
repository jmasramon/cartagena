(ns cartagena.data-abstractions.game
  (:require
   [clojure.data.generators :refer [rand-nth shuffle]]
   [cartagena.core :refer [def-num-players num-cards card-types pirate-colors]]
   [cartagena.data-abstractions.player :as p :refer [make-player color set-cards]]
   [cartagena.data-abstractions.deck :refer [random-deck remove-card-from]]
   [cartagena.data-abstractions.board :as b :refer [make-board boat]]))

;; Data structures
;; example of full game data (state of the game)
;; {:players [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
;;            {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}]
;;  :turn-order {:green :red, :red :yellow, :yellow :green}  
;;  :turn :green
;;  :board [{:pieces {:green 6, :red 6, :yellow 6}, :type :start}
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :bottle}
;;          ...
;;          {:pieces {:green 0, :red 0, :yellow 0}, :type :boat}]
;;  :deck [:flag :sword :hat :pistol :bottle :flag :sword :hat :keys :flag :sword :hat :pistolhat :pistol :bottle :flag :sword :hat]}

;; Functions that need to know how game is implemented
;; game-state: {players turn-order turn board deck}
(defn random-initial-turn
  "Choose a new random color out of the players colors"
  [turns-reservoir]
  (rand-nth (seq turns-reservoir)))

(defn make-turn-order
  "Define the random order in which the players will play"
  [players-colors]
  (let [random-ordered-players-colors (shuffle players-colors)]
    (zipmap random-ordered-players-colors
            (rest (cycle random-ordered-players-colors)))))

;; TODO: do not repeat the algorith make one by using the other
(defn make-random-players
  "Make a list of num players"
  ([num]
   (make-random-players num pirate-colors num-cards card-types))
  ([num players-reservoir cardNum cards-reservoir]
   (vec (for [a-color (take num players-reservoir)]
          (make-player a-color
                       (random-deck cardNum 
                                    cards-reservoir))))))

(defn colors
  "Get list of colors from list of players"
  [players]
  (map color players))

(defn make-game
  "Create a new game"
  ([]
   (make-game def-num-players))
  ([num-players]
   (let [players (make-random-players num-players)
         players-colors (colors players)]
     {:players players
      :turn-order (make-turn-order players-colors)
      :turn (random-initial-turn players-colors)
      :deck (random-deck)
      :board (make-board players)})))

;; getters
(defn players [game]
  (game :players))

(defn player
  "Get one player from a list of players"
  [players color]
  (first (filter #(= color (first (keys %)))
                 players)))

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

(defn actions
  "Get the actions of the player"
  [players color]
  (:actions (color (player players color))))

(defn cards
  "Get the cards of a player"
  [players color]
  (:cards (color (player players color))))

;; testers
(defn player-has-card?
  "Does the player have a card?"
  [players color card]
  (let [player (player players color)]
    (= card (some #{card} (:cards (color player))))))

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
;; TODO: this function makes no sense. Remove it
(defn update-player
  "Updates a player if it is of a color"
  [color newPlayer player]
  (if (= color (first (keys player)))
    newPlayer
    player))

(defn update-player-in-players
  "Changes a player in the list of players"
  [players color newPlayer]
  (map (partial update-player color newPlayer) players))

(defn add-random-card-to-player
  [players color]
  (let [player (player players color)
        new-player (p/add-random-card-to-player player)]
    (update-player-in-players players color new-player)))

(defn reset-actions
  "Puts actions back to 3"
  [players color]
  (let [player (player players color)
        newPlayer (assoc-in player [color :actions] 3)]
    (update-player-in-players players color newPlayer)))

(defn decrease-actions
  "Takes one action from the player"
  [players color]
  (let [player (player players color)
        newPlayer (update-in player [color :actions] dec)]
    (update-player-in-players players color newPlayer)))

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
  (let [color (active-player-color game)
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
        cards (p/cards active-player)
        new-cards (remove-card-from cards card)
        updated-player (set-cards active-player new-cards)
        updated-players (update-player-in-players players color updated-player)]
    (set-players game updated-players)))


