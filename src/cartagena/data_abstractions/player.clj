(ns cartagena.data-abstractions.player)

;; Functions that need to know how player is implemented
;; players:
;;  [{:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}}
;;   {:green {:cards '(:keys :bottle :pistol :bottle :keys :sword), :actions 2}}]
;; player:
;;  {color cards actions} ex: {:yellow {:cards '(:bottle :keys :pistol :bottle :keys :sword), :actions 0}

(defn make-player [color cards])

;; setters
(defn set-cards [player cards]
  (let [color (first (keys player))]
    (assoc-in player [color :cards] cards)))

(defn set-color [player color])

(defn set-actions [player actions])

;; getters
(defn player-color [player]
  (first (keys player)))

(defn player-cards [player]
  (:cards (first (vals player))))

(defn update-player [color newPlayer player]
  (if (= color (first (keys player)))
    newPlayer
    player))

(defn player [players color]
	; (= color (first (map first (map keys players)))))
  (first (filter #(= color (first (keys %)))
                 players)))

(defn actions [players color]
  (:actions (color (player players color))))

(defn cards [players player-color]
  (((player players player-color) player-color) :cards))

