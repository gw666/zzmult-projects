(ns run
 (:gen-class))

(defn -main []
    (compile 'clojure.examples.hello)
    (clojure.examples.hello "gerbils")
    (println *compile-path*)
    (println "Goodbye!"))