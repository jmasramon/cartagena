(ns cartagena.data-abstractions.square
  (:require [cartagena.core :refer [num-starting-pieces]]))

;; Functions that need to know how square is implemented
;; square is implemented as a map {:pieces pieces_map, :type type_set_instance}
;; where pieces_map is {:green green_num, :red red_num, :yellow yellow_num} with all colors of players in game
;; and type_set_instance is one of #{:hat :flag :pistol :sword :bottle :keys} plus :start and :boat
;; So functions of its api should be:
;;   constructor piece (needs type and colors of players)
;;   get/add/remove pieces
;;   get type (never changes)

(defn- make-pieces
  "Creates the empty-pieces hashmap for a square"
  [used-colors num-pieces]
  {:pieces (into (sorted-map) (zipmap used-colors (repeat num-pieces)))})

(defn- make-empty-pieces
  "Creates the empty-pieces hashmap for a square"
  [used-colors]
  (make-pieces used-colors 0))

(defn- make-starting-square
  "creates the start square"
  [used-colors]
  (merge (make-pieces used-colors num-starting-pieces)
         {:type :start}))

(defn make-square
  "Creates a square of specific type"
  [type used-colors]
  (let [empty-pieces (make-empty-pieces used-colors)]
    (case type
      :start (make-starting-square used-colors)
      :end (merge empty-pieces {:type :boat})
      (merge empty-pieces {:type type}))))

;; setters
(defn add-piece-to
  "Add a piece of a certain color;
   actually returns new square with one more piece"
  [square color]
  (update-in square [:pieces color] inc))

(defn remove-piece-from
  "Remove a piece of a certain color; 
   actually returns new square with one less piece"
  [square color]
  (update-in square [:pieces color] dec))

;; getters
;; TODO: Should not be using get-in using board. Should not know anything about board at this data abstraction level
(defn type-of
  "Get type of square"
  [square]
  (square :type))

(defn pieces-in
  "Get hashmap of pieces"
  [square]
  (square :pieces))

(defn num-pieces-in
  "Number of pieces (of a certain color)"
  ([square]
   (reduce + (vals (pieces-in square))))
  ([square color]
   (get-in square [:pieces color])))

;; testers
(defn square-of-type?
  "Is the square of a certain type?"
  [type square]
  (= (type-of square) type))

(defn is-square?
  "Is the data structure a square?"
  [data-structure]
  (and (map? data-structure)
       (= 2 (count data-structure))
       (instance? clojure.lang.Keyword (data-structure :type))
       (map? (data-structure :pieces))))

;; helpers
(defn pieces-to-vector
  "Convert the given pieces hashmap to a vector"
  [pieces]
  (vec (reduce concat (map #(repeat (val %) (key %)) pieces))))

(defn square-pieces-as-vector
  "Convert the pieces hashmap from the given square into a vector"
  [square]
  (pieces-to-vector (pieces-in square)))



