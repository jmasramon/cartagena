(ns cartagena.data-abstractions.game
  (:require
   [clojure.data.generators :as gen]
   [cartagena.core :refer [def-num-players num-cards card-types pirate-colors]]
   [cartagena.data-abstractions.square-bis :as s :refer [pieces-numbers-list-in]]
   [cartagena.data-abstractions.player-bis :as p :refer [make-player color set-cards]]
   [cartagena.data-abstractions.deck :as d :refer [random-deck remove-card-from]]
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

;; TODO: make as many functions as possible private
;; TODO: most public functions shuold receive and return a game

;; Functions that need to know how game is implemented
;; game-state: {players turn-order turn board deck}

;; builders
(defn- random-initial-turn
  "Choose a new random color out of the players colors"
  [turns-reservoir]
  (gen/rand-nth (seq turns-reservoir)))

(defn- make-turn-order
  "Define the random order in which the players will play"
  [players-colors]
  (let [random-ordered-players-colors (gen/shuffle players-colors)]
    (zipmap random-ordered-players-colors
            (rest (cycle random-ordered-players-colors)))))

(defn- make-random-players
  "Make a list of num players"
  ([num]
   (make-random-players num pirate-colors num-cards card-types))
  ([num players-reservoir cardNum cards-reservoir]
   (vec (for [a-color (take num players-reservoir)]
          (make-player a-color
                       (random-deck cardNum
                                    cards-reservoir))))))

(declare colors)

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

(defn- player
  "Get one player from a list of players"
  [players color]
  (first (filter #(= color (p/color %))
                 players)))

(defn- colors
  "Get list of colors from list of players"
  [players]
  (map color players))

(defn turn-order [game]
  (game :turn-order))

(defn turn [game]
  (game :turn))

(defn next-turn [game]
  ((turn game) (turn-order game)))

(defn deck [game]
  (game :deck))

(defn board [game]
  (game :board))

(defn active-player
  "Returns the active player from the game"
  [game]
  (let [turn (turn game)
        players (players game)]
    (first (filter #(= turn (p/color %)) players))))

(defn active-player-color
  "Returns the color of the active play from the game"
  [game]
  (turn game))

(defn next-player-color [game]
  (next-turn game))

(defn active-player-actions
  "Get the actions of the player"
  [game]
  (p/actions (player (players game) (turn game))))

(defn active-player-cards
  "Get the cards of a player"
  [game]
  (p/cards (player (players game) (turn game))))

(defn playable-cards
  "Returns a list with how many cards of each type we have in deck"
  [game]
  (reverse (into '() (d/cards-amounts (active-player-cards game)))))

;; testers
(defn player-has-card?
  "Does the player have a card?"
  [players color card]
  (let [player (player players color)]
    (= card
       (some #{card}
             (p/cards player)))))

(defn winner? [game]
  (let [board (board game)
        boat (boat board)
        pieces (pieces-numbers-list-in boat)]
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

(defn update-player-in-players
  "Changes a player in the list of players"
  [players color new-player]
  (map  #(if (= color (p/color %1)) new-player %1) players))

(defn add-random-card-to-player-in-players
  [players color]
  (let [player (player players color)
        new-player (p/add-random-card-to-player-in-players player)]
    (update-player-in-players players color new-player)))

(defn add-random-card-to-active-player
  "Add a (random) card to the active player"
  [game]
  (let [color (active-player-color game)
        players (players game)
        updated-players (add-random-card-to-player-in-players players color)]
    (set-players game updated-players)))

(defn decrease-actions
  "Takes one action from the player"
  [players color]
  (let [player (player players color)
        newPlayer (p/decrease-actions player)]
    (update-player-in-players players color newPlayer)))

(defn decrease-actions-from-active-player
  "Takes one action from the player"
  [game]
  (let [color (active-player-color game)
        players (players game)
        player (player players color)]
    (set-players game
                 (update-player-in-players players color
                                           (p/decrease-actions player)))))

(defn reset-actions
  "Puts one player's actions in players back to 3"
  [players color]
  (let [player (player players color)
        newPlayer (p/reset-actions player)]
    (update-player-in-players players color newPlayer)))

(defn pass-turn
  "Pass a turn by
   1- set 3 actions to next user 
   2- set turn to next user"
  [game]
  (let [next-player-color (next-player-color game)
        new-players (reset-actions (players game) next-player-color)]
    (set-turn
     (set-players game new-players)
     next-player-color)))

(defn move-piece
  "Moves a piece from to
   1-gets board 
   2-moves piece in board
   3-updates board in game"
  [game from to]
  (let [board (board game)
        color (active-player-color game)
        new-board (b/move-piece board from to color)]
    (set-board game new-board)))

(defn turn-played
  "Plays a turn by:
   1-Reduce the num of actions from the active player
   2- If zero remaining, 
     2-1- set 3 actions to next user 
     2-1- set turn to next user"
  [game]
  (let [color (active-player-color game)
        decreased-action-game (decrease-actions-from-active-player game)
        remaining-actions (active-player-actions decreased-action-game)]
    (if (= 0 remaining-actions)
      (pass-turn decreased-action-game)
      decreased-action-game)))

(defn remove-played-card
  "Removes card played from active user
   1-gets the active player's cards
   2-removes the card
   3-updates player in game"
  [game card]
  (let [players (players game)
        active-player (active-player game)
        color (color active-player)
        cards (p/cards active-player)
        new-cards (remove-card-from cards card)
        updated-player (set-cards active-player new-cards)
        updated-players (update-player-in-players players color updated-player)]
    (set-players game updated-players)))

(defn available-fall-back?
  [game from]
  (let [board (board game)
        to (b/index-closest-nonempty-slot board
                                          from)]
    (not (nil? to))))

(defn available-piece?
  [game square-index]
  (let [board (board game)
        player-color (active-player-color game)
        origin-square (b/square board square-index)
        available-pieces (s/num-pieces-in origin-square player-color)]
    (and
     (not (nil? available-pieces))
     (> available-pieces 0))))

(defn fallback-square-index
  [game from]
  (let [board (board game)]
    (b/index-closest-nonempty-slot board
                                   from)))

(defn next-empty-slot-index
  [game card from]
  (let [board (board game)]
    (b/next-empty-slot-index board
                             card
                             from)))