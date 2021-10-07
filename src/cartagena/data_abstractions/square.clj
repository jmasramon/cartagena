(ns cartagena.data-abstractions.square)

;; Functions that need to know how square is 
;; square is implemented as a map {:pieces pieces_map, :type type_set_instance}
;; where pieces_map is {:green green_num, :red red_num, :yellow yellow_num}
;; and type_set_instance is #{:hat :flag :pistol :sword :bottle :keys} plus :start and :boat


(defn starting-pieces [used-colors]
  {:pieces (into (sorted-map) (zipmap used-colors (repeat 6)))
   :type :start})

(defn empty-pieces [used-colors]
  {:pieces (into (sorted-map) (zipmap used-colors (repeat 0)))})

(defn make-square
  [type used-colors]
  (case type
    :start (starting-pieces used-colors)
    :end (merge (empty-pieces used-colors) {:type :boat})
     (merge (empty-pieces used-colors) {:type type})))

(defn new-board-section [types used-colors]
  (let [shuffled-types (shuffle (vec types))]
    (vec (for [type shuffled-types]
           (merge (empty-pieces used-colors)
                  {:type type})))))

;; setters
(defn inc-square-players [square color]
  (update-in square [:pieces color] inc))

(defn dec-square-players [square color]
  (update-in square [:pieces color] dec))

;; getters
;; TODO: Should not be using get-in using board. Should not know anything about board at this data abstraction level
(defn square-type [board index]
  (get-in board [index :type]))

(defn square-pieces
  ([board index]
   (get-in board [index :pieces]))
  ([board index color]
   (get-in board [index :pieces color])))

(defn num-pieces [square color]
  (get-in square [:pieces color]))

(defn square-contents-vector
  "Convert the pieces on the given square to a vector of pieces"
  ([square]
   (vec (reduce concat (map #(repeat (val %) (key %)) square))))
  ([board index]
   (square-contents-vector (square-pieces board index))))

(defn square-of-type? [square type]
  (= (square :type) type))

