(ns cartagena.setters.sdariEntities
    (:require   [cartagena.getters.sdariEntities :refer [square]]
                [cartagena.setters.otherEntities :refer [update-player]]
        ))

(defn update-player-in-players [players color newPlayer]
    (map (partial update-player color newPlayer) players))

(defn inc-square-players [square color]
    (update-in square [:pieces color] inc ))

(defn dec-square-players [square color]
    (update-in square [:pieces color] dec ))

(defn add-piece [board index color]
    (let [square (square board index)]
        (assoc-in board [index]
            (inc-square-players square color))))

(defn remove-piece [board index color]
    (let [square (square board index)]
        (assoc-in board [index]
            (dec-square-players square color))))

