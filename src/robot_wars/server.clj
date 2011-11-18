(ns robot-wars.server
  (:use [clojure.java.io :only [reader writer]]
        [clojure.contrib.server-socket :only [create-server]]))

(def *inventory*)
(def *player-name*)
(def prompt "> ")
(def player-streams (ref {}))

(defn- carrying? [item]
  (some #{(keyword item) @*inventory*}))

(defn- get-unique-player-name [name]
  name)

(defn- robot-wars-handle-client [in out]
  (binding [*in* (reader in)
            *out* (writer out)
            *err* (writer System/err)]
    (print "\nWhat is your name? ") (flush)
    (binding [*inventory* (ref #{})]
      (try (loop [input (read-line)]
             (when input
               (println input))
             (.flush *err*)
             (print prompt) (flush)
             (recur (read-line)))
           (finally (print "\nGood-bye."))))))

(defn -main
  ([port dir]
     (defonce server (create-server (Integer. port) robot-wars-handle-client))
     (println "Launching **Robot Wars** on port" port))
  ([port] (-main port ""))
  ([] (-main 3333)))