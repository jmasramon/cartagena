(ns cartagena.data-abstractions.player
  (:require
   [cartagena.core :refer [starting-actions card-types]]
   [cartagena.data-abstractions.deck :refer [random-card add-card]]))

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

(defn make-player
  "Make a player with a color and a list of cards. It has 3 actions"
  [color cards]
  {color {:actions starting-actions, :cards cards}})

;; getters
;; TODO: this one belongs to the game?
(defn color
  "Get the color of the player"
  [player]
  (first (keys player)))

(defn actions
  "Get the actions of the player"
  [player]
  (let [color (color player)]
    (:actions (color player))))

(defn cards
  "Get the cards of the player"
  ([player]
   (:cards (first (vals player)))))

;; testers
(defn player-has-card?
  "Does the player have a card?"
  [player card]
  (let [color (color player)]
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
  [player]
  (let [cards (cards player)
        new-card (random-card card-types)
        new-cards (add-card cards new-card)]
    (set-cards player new-cards)))

(defn reset-actions
  "Puts actions back to 3"
  [player]
  (let [color (color player)]
    (assoc-in player [color :actions] starting-actions)))

(defn decrease-actions
  "Takes one action from the player"
  [player]
  (let [color (color player)]
    (update-in player [color :actions] dec)))



