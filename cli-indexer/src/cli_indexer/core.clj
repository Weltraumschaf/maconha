(ns cli-indexer.core
  (:require [clojure.string :as string]
            [clojure.tools.cli :as cli]
            [clojure.java.io :as io]
            [cli-indexer.app :as app])
  (:gen-class))

(def cli-options
  [["-c" "--config FILE" "Path to YAML configuration file."]
   ["-v" nil "Verbosity level; may be specified multiple times to increase value."
    ;; If no long-option is specified, an option :id must be given
    :id :verbosity
    :default 0
    :update-fn inc]
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["This is the cli command to generate the index for Maconha Search."
        ""
        "Usage: maconha-indexer [options]"
        ""
        "Options:"
        options-summary
        ""
        "Please refer to the manual page for more information."]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn validate-args
  "Validate command line arguments. Either return a map indicating the program
  should exit (with a error message, and optional ok status), or a map
  indicating the action the program should take and the options provided."
  [args]
  (let [{:keys [options errors summary]} (cli/parse-opts args cli-options)]
    (cond
      (:help options)                                       ; help => exit OK with usage summary
      {:exit-message (usage summary) :ok? true}
      errors                                                ; errors => exit with description of errors
      {:exit-message (error-msg errors)}
      ;; Check if config file exists:
      (.exists (io/file (:config options)))                 ; FIXME PRint error message that file does not exist.
      {:options options}
      :else                                                 ; failed custom validation => exit with usage summary
      {:exit-message (usage summary)})))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (let [{:keys [options exit-message ok?]} (validate-args args)]
    (if exit-message
      (exit (if ok? 0 1) exit-message)
      (app/run options))))
