(ns cartagena.setters.mainEntities)

(defn set-players [game-state players]
    (assoc-in game-state [:players] players))

(defn set-turn-order [game-state turn-order]
    (assoc-in game-state [:turn-order] turn-order))

(defn set-turn [game-state turn]
    (assoc-in game-state [:turn] turn))

(defn set-board [game-state board]
    (assoc-in game-state [:board] board))

(defn set-deck [game-state deck]
    (assoc-in game-state [:deck] deck))

