(ns cartagena.setters.otherEntities
    )

(defn update-player [color newPlayer player]
    (if (= color (first (keys player)))
        newPlayer
        player))

(defn set-cards [player cards]
    (let [color (first (keys player))]
        (assoc-in player [color :cards] cards)))
