(ns clojure.examples.instance
    (:gen-class
     :implements [java.util.Iterator]
     :init init
     :constructors {[String] []}
     :state state))

(defn -init [s]
  [[] (ref {:s s :index 0})])

(defn -hasNext [this]
  (let [{:keys [s index]} @(.state this)]
    (< index (count s))))

(defn -next [this]
  (let [{:keys [s index]} @(.state this)
        ch (.charAt s index)]
    (dosync (alter (.state this) assoc :index (inc index)))
    ch))

(gen-interface
 :name clojure.examples.IBar
 :methods [[bar [] String]])

(gen-class
 :name clojure.examples.impl
 :implements [clojure.examples.IBar]
 :prefix "impl-"
 :methods [[foo [] String]])

(defn impl-foo [this]
  (str (class this)))

(defn impl-bar [this]
  (str "I " (if (instance? clojure.examples.IBar this)
              "am"
              "am not")
       " an IBar"))

(defn -main [s]
  (let [x (new clojure.examples.instance s)
        y (new clojure.examples.impl)]
    (while (.hasNext x)
      (println (.next x)))
    (println (.foo y))
    (println (.bar y))))
