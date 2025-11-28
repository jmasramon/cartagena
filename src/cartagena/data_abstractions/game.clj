(ns cartagena.data-abstractions.game
  (:require
   [clojure.data.generators :as gen]
   [cartagena.data-abstractions.player-bis :as p]
   [cartagena.data-abstractions.deck :as d]
   [cartagena.data-abstractions.board :as b]))

;; ABSTRACTION LAYER: Layer 3 (Game State)
;;
;; This namespace represents the complete game state and provides operations
;; for querying and updating it. It depends ONLY on:
;;   - Layer 0: cartagena.core (game constants)
;;   - Layer 1: cartagena.data-abstractions.player-bis (player operations)
;;              cartagena.data-abstractions.deck (deck operations)
;;   - Layer 2: cartagena.data-abstractions.board (board operations)
;;
;; CRITICAL: This layer never directly manipulates squares or lower-level
;; structures. All board operations go through the board namespace API.
;; All player operations go through the player namespace API.
;;
;; Higher layers (moves, UI) interact with game state through this namespace's
;; public API, treating the game as an immutable value.
;;
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

;; Number of cards each player gets.
(def NUM-CARDS 6)

;; Default number of players if none is specified.
(def DEF-NUM-PLAYERS 3)

;; Available player colors.
(def PIRATE-COLORS #{:red :green :yellow :blue :brown})

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
   (mapv (fn [a-color]
           (p/make-player a-color
                          (d/random-deck NUM-CARDS)))
         (take num PIRATE-COLORS))))

(declare colors)

(defn make-game
  "Create a new game"
  ([]
   (make-game DEF-NUM-PLAYERS))
  ([num-players]
   (let [players (make-random-players num-players)
         players-colors (colors players)]
     {:players players
      :turn-order (make-turn-order players-colors)
      :turn (random-initial-turn players-colors)
      :deck (d/random-deck)
      :board (b/make-board players)})))

;; getters
(defn players [game]
  (:players game))

(defn- player
  "Get one player from a list of players"
  [players color]
  (first (filter #(= color (p/color %))
                 players)))

(defn- colors
  "Get list of colors from list of players"
  [players]
  (map p/color players))

; to hide implementation details from external namespaces
(defn turn-order [game]
  (:turn-order game))

(defn turn [game]
  (:turn game))

(defn next-turn [game]
  ((turn game) (turn-order game)))

(defn deck [game]
  (:deck game))

(defn board [game]
  (:board game))

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
  (->> game
       active-player-cards
       d/cards-amounts
       (sort-by first)))

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
        boat-index (dec (count board))
        pieces (b/pieces-numbers-list-in board boat-index)]
    (pos? (count (filter #(= % NUM-CARDS) pieces)))))

;; setters
(defn set-players [game players]
  (assoc game :players players))

(defn set-turn-order [game turn-order]
  (assoc game :turn-order turn-order))

(defn set-turn [game turn]
  (assoc game :turn turn))

(defn set-board [game board]
  (assoc game :board board))

(defn set-deck [game deck]
  (assoc game :deck deck))

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
        new-player (p/decrease-actions player)]
    (update-player-in-players players color new-player)))

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
        new-player (p/reset-actions player)]
    (update-player-in-players players color new-player)))

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
  (let [decreased-action-game (decrease-actions-from-active-player game)
        remaining-actions (active-player-actions decreased-action-game)]
    (if (zero? remaining-actions)
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
        color (p/color active-player)
        cards (p/cards active-player)
        new-cards (d/remove-card-from cards card)
        updated-player (p/set-cards active-player new-cards)
        updated-players (update-player-in-players players color updated-player)]
    (set-players game updated-players)))

(defn available-fall-back?
  [game from]
  (let [board (board game)
        to (b/index-closest-nonempty-slot board
                                          from)]
    (some? to)))

(defn available-piece?
  [game square-index]
  (let [board (board game)
        player-color (active-player-color game)
        available-pieces (b/num-pieces-in board square-index player-color)]
    (and
     (some? available-pieces)
     (pos? available-pieces))))

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