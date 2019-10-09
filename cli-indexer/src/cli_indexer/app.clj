(ns cli-indexer.app
  (:require [clojure.java.io :as io]
            [clj-yaml.core :as yaml]))

(defn run [options]
  (println "Hello, World!")
  (println (yaml/parse-string (slurp (get options :config))))
  )