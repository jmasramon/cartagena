(ns cartagena.data-abstractions.player
  (:require
   [cartagena.core :refer [starting-actions def-num-players pirate-colors num-cards card-types]]
   [cartagena.data-abstractions.deck :refer [random-deck]]))

;; Functions that need to know how player is implemented
;; player:
;;  {color cards actions} ex: {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}
;; players:
;;  [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
;;   {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}]

(defn make-player [color cards])

(defn random-players
  ([num]
  (vec (for [x (take num pirate-colors)]
         {x {:cards (random-deck num-cards card-types)
             :actions starting-actions}})))
  ([num players-reservoir cardNum cards-reservoir]
  (vec (for [x (take num players-reservoir)]
         {x {:cards (random-deck cardNum cards-reservoir)
             :actions starting-actions}}))))

;; getters
(defn player-color [player]
  (first (keys player)))

(defn players-colors [players]
  (flatten (map keys players)))

(defn player-cards [player]
  (:cards (first (vals player))))


;; TODO: this one belongs to the game?
(defn player [players color]
	; (= color (first (map first (map keys players)))))
  (first (filter #(= color (first (keys %)))
                 players)))

(defn player-has-card? [players color card]
  (let [player (player players color)]
    (= card (some #{card} (:cards (color player))))))

(defn players-turn? [players color]
  (= 3 (:actions (color (player players color)))))


(defn actions [players color]
  (:actions (color (player players color))))

(defn cards [players player-color]
  (:cards ((player players player-color) player-color)))
;; setters
(defn set-cards [player cards]
  (let [color (first (keys player))]
    (assoc-in player [color :cards] cards)))

(defn set-color [player color])

(defn set-actions [player actions])

;; TODO: this function makes no sense. Remove it
(defn update-player [color newPlayer player]
  (if (= color (first (keys player)))
    newPlayer
    player))

(defn update-player-in-players [players color newPlayer]
  (map (partial update-player color newPlayer) players))

(defn add-turns [players color]
  (let [player (player players color)
        newPlayer (assoc-in player [color :actions] 3)]
    (update-player-in-players players color newPlayer)))

(defn decrease-turns [players color]
  (let [player (player players color)
        newPlayer (update-in player [color :actions] dec)]
    (update-player-in-players players color newPlayer)))



