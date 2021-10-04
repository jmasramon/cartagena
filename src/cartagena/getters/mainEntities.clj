(ns cartagena.getters.mainEntities)

;; game
(defn players [game-state]
    (game-state :players))

(defn turn-order [game-state]
    (game-state :turn-order))

(defn turn [game-state]
    (game-state :turn))

(defn nex-turn [game-state]
    ((turn game-state) (turn-order game-state)))

(defn board [game-state]
    (game-state :board))

(defn deck [game-state]
    (game-state :deck))

