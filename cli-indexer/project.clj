(defproject cli-indexer "1.0.0-SNAPSHOT"
  :description "CLI tool to generate the file index for Maconha search."
  :url "https://github.com/weltraumschaf/maconha"
  :license {:name "\"THE BEER-WARE LICENSE\" (Revision 43)"
            :url  "https://www.weltraumschaf.de/the-beer-ware-license.txt"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/tools.cli "0.4.2"]]
  :plugins [[lein-binplus "0.6.5"]]
  :bin {:name "cli-indexer"}
  :main cli-indexer.core
  :profiles {:uberjar {:aot :all}})
