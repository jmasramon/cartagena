(ns cartagena.data-abstractions.player
  (:require
   [cartagena.core :refer [starting-actions pirate-colors num-cards card-types]]
   [cartagena.data-abstractions.deck :refer [random-card add-card random-deck]]))

;; Functions that need to know how player is implemented
;; player:
;;  {color cards actions} ex: {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}
;; players:
;;  [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
;;   {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}]
;; Internal entities (concepts) are:
;;   color  (constant; unchangeable)
;;   actions 
;;   cards

(defn- make-player
  "Make a player with a color and a list of cards. It has 3 actions"
  [color cards]
  {color {:actions starting-actions, :cards cards}})

(defn random-players
  "Make a number of players"
  ([num]
   (let [cards (random-deck num-cards card-types)]
     (vec (for [color (take num pirate-colors)]
            (make-player color cards)))))
  ([num players-reservoir cardNum cards-reservoir]
   (let [cards (random-deck cardNum cards-reservoir)]
     (vec (for [color (take num players-reservoir)]
            (make-player color cards))))))

;; getters
;; TODO: this one belongs to the game?
(defn player
  "Get one player from a list of players"
  [players color]
  (first (filter #(= color (first (keys %)))
                 players)))

(defn color
  "Get the color of the player"
  [player]
  (first (keys player)))

(defn colors
  "Get list of colors from list of players"
  [players]
  (flatten (map keys players)))

(defn actions
  "Get the actions of the player"
  ([player]
   (:actions player))
  ([players color]
   (:actions (color (player players color)))))

(defn cards
  "Get the cards of the player"
  ([player]
   (:cards (first (vals player))))
  ([players player-color]
   (:cards ((player players player-color) player-color))))

;; testers
(defn player-has-card?
  "Does the player have a card?"
  [players color card]
  (let [player (player players color)]
    (= card (some #{card} (:cards (color player))))))

;; setters
(defn set-cards [player cards]
  (let [color (first (keys player))]
    (assoc-in player [color :cards] cards)))

(defn set-actions [player actions]
  (assoc-in player [(color player) :actions] actions))

;; TODO: this function makes no sense. Remove it
(defn- update-player
  "Updates a player if it is of a color"
  [color newPlayer player]
  (if (= color (first (keys player)))
    newPlayer
    player))

;; this function is too white-box
;; it forces external modules to know how the player
;; is implemented
(defn update-player-in-players
  "Changes a player in the list of players"
  [players color newPlayer]
  (map (partial update-player color newPlayer) players))

(defn add-random-card-to-player
  ([player]
   (let [cards (cards player)
         new-card (random-card card-types)
         new-cards (add-card cards new-card)]
     (set-cards player new-cards)))
  ([players color]
   (let [player (player players color)
         new-player (add-random-card-to-player player)]
     (update-player-in-players players color new-player))))

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



