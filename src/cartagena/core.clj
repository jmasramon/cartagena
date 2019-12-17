(ns cartagena.core
    (:require [clojure.set :refer [union]]))

(def pirate-colors #{:red :green :yellow})
(def card-types #{:hat :flag :pistol :sword :bottle :keys})
(def board-steps-types (union #{:start :finish} card-types))
(def turns {:yellow :blue, :blue :green, :green :yellow})
(def starting-actions 3)
(def actions #{:advance :retreat})

