(ns cartagena.data-abstractions.board-test
  (:require [clojure.test :refer [deftest is testing]]
            [clojure.data.generators :refer [*rnd*]]
            [cartagena.data-abstractions.player-bis :refer [make-player]]
            [cartagena.data-abstractions.deck :refer [random-deck]]
            [cartagena.data-abstractions.square-bis :as sq :refer [type-of is-square?]]
            [cartagena.data-abstractions.board :as b]))

(deftest make-board-section-test
  (testing "make-board-section"
    (let [make-board-section #'cartagena.data-abstractions.board/make-board-section
          a-section (make-board-section b/SQUARE-TYPES
                                        [:yellow :green :red])]
      (is (= true
             (vector? a-section))
          "should return a vector")
      (is (= 6
             (count a-section))
          "should return 6 squares")
      (is (= true
             (reduce (fn [acc element] (and acc element)) true (map is-square? a-section)))
          "all elements should be squares")
      (is (= 6
             (count (into #{} (map type-of a-section))))
          "should not have repeated types of squares")
      (is (= false
             (contains? (into #{} (map type-of a-section))
                        :start))
          "should not have :start")
      (is (= false
             (contains? (into  #{} (map type-of a-section))
                        :boat))
          "should not have :boat")
      (is (= 0
             (reduce + (map sq/num-pieces-in a-section)))
          "all sections should be empty"))))

(deftest make-board-test
  (testing "make-board"
    (binding [*rnd* (java.util.Random. 12345)]
      (let [board (b/make-board   b/SQUARE-TYPES
                                  [(make-player :yellow (random-deck) 0)
                                   (make-player :green (random-deck) 2)])]
        (is (= 38 (count board)))
        (is (= :start (b/square-type board 0)))
        (is (= 2 (count (sq/pieces-in (first board)))))
        (is (= 12 (sq/num-pieces-in (first board))))
        (let [subsections (subvec board 1 (count board))]
          (is (= false (contains? (into subsections
                                        #{})
                                  :start)))
          (is (= false (contains? (into subsections
                                        #{})
                                  :boat))))
        (is (= :boat (type-of (last board))))
        (is (= 12 (reduce + (map sq/num-pieces-in board))))))))

(def board
  (binding [*rnd* (java.util.Random. 12345)]
    (b/add-piece-to
     (b/add-piece-to
      (b/make-board   b/SQUARE-TYPES
                      [(make-player :yellow (random-deck) 0)
                       (make-player :green (random-deck) 2)
                       (make-player :red (random-deck) 2)])
      8
      :green)
     8
     :red)))

(deftest getters-tests
  (testing "boat"
    (is (=  :boat
            (type-of (b/boat board)))))
  (testing "square"
    (is (=  :start
            (type-of (b/square board
                               0))))
    (is (=  :hat
            (type-of (b/square board
                               4)))))
  (testing "pieces-in"
    (is (=  {:green 6, :red 6, :yellow 6}
            (b/pieces-in board
                         0))
        "should return start pieces")
    (is (=  {:green 0, :red 0, :yellow 0}
            (b/pieces-in board
                         (dec (count board))))
        "should return boat pieces")
    (is (=  {:green 1, :red 1, :yellow 0}
            (b/pieces-in board
                         8))
        "should return pieces"))
  (testing "num-pieces-in"
    (is (=  18
            (b/num-pieces-in board
                             0))
        "should return start pieces")
    (is (=  0
            (b/num-pieces-in board
                             (dec (count board))))
        "should return boat pieces")
    (is (=  2
            (b/num-pieces-in board
                             8))
        "should return pieces")
    (is (=  1
            (b/num-pieces-in board
                             8
                             :green))
        "should only green pieces"))
  (testing "square-pieces-as-vector"
    (is (=  [:green :green :green :green :green :green :red :red :red :red :red :red :yellow :yellow :yellow :yellow :yellow :yellow]
            (b/square-pieces-as-vector board
                                       0))
        "should return start pieces")
    (is (=  [:green :red]
            (b/square-pieces-as-vector board
                                       8))
        "should return boat pieces"))
  (testing "square-type"
    (is (=  :start
            (b/square-type board
                           0))
        "should :start")
    (is (=  :hat
            (b/square-type board
                           7))
        "should return :boat"))
  (testing "squares-of-type"
    (is (=  1
            (count (b/squares-of-type board
                                      :start))
            (count (b/squares-of-type board
                                      :boat)))
        "should :start")
    (is (=  6
            (count (b/squares-of-type board
                                      :sword)))
        "should return :boat"))
  (testing "indexes-squares-of-type"
    (is (= '(0)
           (b/indexes-squares-of-type board
                                      :start))
        "should :start")
    (is (= '(6 12 13 19 25 31)
           (b/indexes-squares-of-type board
                                      :sword))
        "should return :boat")
    (is (= '(4 7 16 20 27 34)
           (b/indexes-squares-of-type board
                                      :hat))
        "should return :boat")))

(deftest space-available?-test
  (testing "space-available?"
    (is (= false
           (b/space-available? board
                               0)))
    (is (= true
           (b/space-available? board
                               1)))
    (is (= true
           (b/space-available? board
                               8)))))

(deftest square-full?-test
  (testing "square-full?"
    (is (= true
           (b/square-full? board
                           0)))
    (is (= false
           (b/square-full? board
                           1)))
    (is (= false
           (b/square-full? board
                           8)))))

(deftest square-has-color?-test
  (testing "square-has-color?"
    (is (= false
           (b/square-has-color? board
                                3
                                :green)))
    (is (= true
           (b/square-has-color? board
                                0
                                :red)))
    (is (= true
           (b/square-has-color? board
                                8
                                :red)))
    (is (= false
           (b/square-has-color? board
                                8
                                :yellow)))))

(deftest add-piece-to-test
  (testing "add-piece-to"
    (let [new-board (b/add-piece-to board
                                    1
                                    :red)
          modified-square (nth new-board 1)
          original-square (nth board 1)]
      (is (= 0
             (sq/num-pieces-in original-square)))
      (is (= 1
             (sq/num-pieces-in modified-square)))
      (is (= 1
             (sq/num-pieces-in modified-square :red)))
      (is (= 0
             (sq/num-pieces-in modified-square :green)))
      (is (= 0
             (sq/num-pieces-in modified-square :yellow))))))

(deftest remove-piece-from-test
  (testing "remove-piece-from"
    (let [new-board (b/remove-piece-from board
                                         8
                                         :green)
          modified-square (nth new-board 8)
          original-square (nth board 8)]
      (is (= 2
             (sq/num-pieces-in original-square)))
      (is (= 1
             (sq/num-pieces-in modified-square)))
      (is (= 1
             (sq/num-pieces-in original-square :red)))
      (is (= 1
             (sq/num-pieces-in original-square :green)))
      (is (= 0
             (sq/num-pieces-in original-square :yellow)))
      (is (= 1
             (sq/num-pieces-in modified-square :red)))
      (is (= 0
             (sq/num-pieces-in modified-square :green)))
      (is (= 0
             (sq/num-pieces-in modified-square :yellow))))))

(deftest empty-slot?-test
  (testing "empty-slot? two params"
    (is (= true
           (b/empty-slot?
            (nth board 3)
            :bottle
            :green)))
    (is (= true
           (b/empty-slot?
            (nth board 5)
            :keys
            :red)))
    (is (= false
           (b/empty-slot?
            (nth board 8)
            :flag
            :green)))
    (is (= true
           (b/empty-slot?
            (nth board 8)
            :flag
            :yellow))))
  (testing "empty-slot? one param"
    (is (= true
           (b/empty-slot?
            (nth board 3)
            :bottle)))
    (is (= true
           (b/empty-slot?
            (nth board 5)
            :keys)))
    (is (= false
           (b/empty-slot?
            {:pieces {:green 0, :red 0, :yellow 1}, :type :pistol}
            :pistol)))
    (is (= false
           (b/empty-slot?
            (nth board 8)
            :flag)))))

(deftest next-empty-slot-index-test
  (testing "next-empty-slot-index"
    (is (= 5
           (b/next-empty-slot-index board :keys 0 :green)))
    (is (= 3
           (b/next-empty-slot-index board :bottle 0 :red)))

    (is (= 10
           (b/next-empty-slot-index board :keys 6 :yellow)))
    (is (= 10
           (b/next-empty-slot-index board :keys 5 :green)))
    (is (= 5
           (b/next-empty-slot-index board :keys 4 :red)))

    (is (= 8
           (b/next-empty-slot-index board :flag 2 :yellow))))
  (testing "next-empty-slot-index two params"
    (is (= 5
           (b/next-empty-slot-index board :keys 0)))
    (is (= 3
           (b/next-empty-slot-index board :bottle 0)))
    (is (= 10
           (b/next-empty-slot-index board :keys 6)))
    (is (= 10
           (b/next-empty-slot-index board :keys 5)))
    (is (= 5
           (b/next-empty-slot-index board :keys 4)))
    (is (= 15
           (b/next-empty-slot-index board :flag 2)))
    (is (= 15
           (b/next-empty-slot-index board :flag 10)))))

(deftest nonempty-slot?-test
  (testing "nonempty-slot?"
    (is (= true
           (b/nonempty-slot?
            (first board))))

    (is (= false
           (b/nonempty-slot?
            (nth board 2))))

    (is (= true
           (b/nonempty-slot?
            (nth board 8))))

    (is (= false
           (b/nonempty-slot?
            (last board))))))

(deftest index-closest-nonempty-slot-test
  (testing "index-closest-nonempty-slot"
    (is (= nil
           (b/index-closest-nonempty-slot board 3)))
    (is (= nil
           (b/index-closest-nonempty-slot board 4)))
    (is (= nil
           (b/index-closest-nonempty-slot board 5)))
    (is (= nil
           (b/index-closest-nonempty-slot board 6)))
    (is (= nil
           (b/index-closest-nonempty-slot board 7)))
    (is (= nil
           (b/index-closest-nonempty-slot board 8)))
    (is (= 8
           (b/index-closest-nonempty-slot board 9)))
    (is (= 8
           (b/index-closest-nonempty-slot board 10)))
    (is (= 8
           (b/index-closest-nonempty-slot board 11)))
    (is (= nil
           (b/index-closest-nonempty-slot (subvec board 1)
                                          7)))))

(deftest move-piece-test
  (testing "move-piece"
    (let [new-board (b/move-piece board
                                  8
                                  7
                                  :red)]
      (is (= true
             (b/square-has-color? board 8 :red)))
      (is (= false
             (b/square-has-color? board 7 :red)))
      (is (= false
             (b/square-has-color? new-board 8 :red)))
      (is (= true
             (b/square-has-color? new-board 7 :red))))))


