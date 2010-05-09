(ns edu.umd.cs.piccolo.PCanvas.GraphEditor
 (:gen-class
   :extends edu.umd.cs.piccolo.PCanvas
   :state state
   :init init
   :post-init GraphEditorInit
   :constructors {[int int] []})
 (:import
   (edu.umd.cs.piccolo   PCanvas PLayer PNode PRoot)
   (edu.umd.cs.piccolo.event   PBasicInputEventHandler PDragEventHandler
     PInputEvent PInputEventFilter)
   (edu.umd.cs.piccolo.nodes   PPath)
   (edu.umd.cs.piccolo.util   PBounds)
   (java.awt Color Dimension Graphics2D)
   (java.awt.event   InputEvent MouseEvent)
   (java.awt.geom   Point2D)
   (javax.swing   JFrame)
   (java.util   ArrayList Random)
   ))

(defn -init [width height]
  ; returns [ signature of superclass (no args), value of state (a map) ]
  [[] { :num-nodes 5, :num-edges 5, :random (Random.) }])

(defn add-to-node [node edge]
  (let [current-count (.getAttribute node "num-used")
        ]
    (aset (.getAttribute node "edges") current-count edge)
    (println (str "edge " edge " added to " node " at position " current-count))
    (.addAttribute node "num-used" (inc current-count))))

(defn add-to-edge [edge node]
  (let [current-count (.getAttribute edge "num-used")
        ]
    (aset (.getAttribute edge "nodes") current-count node)
    (println (str "node " node " added to " edge " at position " current-count))
    (.addAttribute edge "num-used" (inc current-count))))

(defn -GraphEditorInit [this width height]
  (.setPreferredSize this (Dimension. width height))
  (let [{:keys [num-edges num-nodes random]} (.state this)
        node-layer (.getLayer this)
        edge-layer (PLayer.)
        node-vector   ; its value is result of loop on next line
        (loop [result [], x num-nodes]
          (if (zero? x)
            result   ; returned if true
            (recur (conj result (PPath/createEllipse
                                  (.nextInt random width)
                                  (.nextInt random height)
                                  20
                                  20))
              (dec x))))   ; returned if false
        ;        n1 (first node-vector)
        num-nodes-per-edge   2
        ignore-this 50
        ]

    (defn install-node [node]
      ;      (println node)
      (.addChild node-layer node)
      (.addAttribute node "edges" (make-array PPath num-edges))
      (.addAttribute node "num-used" 0))

    (defn install-edge [edge]
      ;      (println node)
      (.addChild edge-layer edge)
      (.addAttribute edge "nodes" (make-array PPath num-nodes-per-edge))
      (.addAttribute edge "num-used" 0))

    (defn update-edge [edge]
      (let [node1 (aget (.getAttribute edge "nodes") 0)
            node2 (aget (.getAttribute edge "nodes") 1)
            start (.. node1 getFullBoundsReference getCenter2D)
            end   (.. node2 getFullBoundsReference getCenter2D)]
        (.reset edge)
        (println (str "Draw from (" (.getX start) " "  (.getY start)
                   ") to (" (.getX end) " "  (.getY end) ")" ))
        (.moveTo edge (.getX start) (.getY start))
        (.lineTo edge (.getX end) (.getY end))))

    (.addChild (.getRoot this) edge-layer)
    (.addLayer (.getCamera this) 0 edge-layer)

    ;    (println "vector")
    (println node-vector)
    ;    (doall (map install-node node-vector))
    (doall (for [nv node-vector]
             (install-node nv)))

    (defn random-from-num-nodes [_]
      (vector (.nextInt random num-nodes) (.nextInt random num-nodes)))

    (defn not-equal? [pair]
      (not= (nth pair 0) (nth pair 1)))

    (defn process-edge-for-nodes [pair]
      (let [n1 (nth pair 0)
            n2 (nth pair 1)
            edge (PPath.)
            node1 (.getChild node-layer n1)
            node2 (.getChild node-layer n2)]
        (println (str  "Processing node " pair))
        (install-edge edge)
        (add-to-node node1 edge)
        (add-to-node node2 edge)
        (add-to-edge edge node1)
        (add-to-edge edge node2)
        (update-edge edge)
        (println "###### end process-edge-for-nodes ######")))

    (let [random-pair-seq (drop 1 (iterate random-from-num-nodes ignore-this))
          pairs-for-edges (take num-edges (filter not-equal? random-pair-seq))]
      (doall (map process-edge-for-nodes pairs-for-edges))
      (println "Done with creation of nodes and edges"))

    (doto node-layer
      (.addInputEventListener
        (proxy [PDragEventHandler] []
          (let [filter (PInputEventFilter.)]
            (.setOrMask filter (+ InputEvent/BUTTON1_MASK
                                 InputEvent/BUTTON3_MASK))
            (.setEventFilter filter))
          (mouseEntered [e]
            (proxy-super mouseEntered e)
            (if (= (.getButton e) MouseEvent/NOBUTTON)
              (. (. e getPickedNode) setPaint Color/RED)))
          (mouseExited [e]
            (proxy-super mouseExited e)
            (if (= (.getButton e) MouseEvent/NOBUTTON)
              (. (. e getPickedNode) setPaint Color/WHITE)))
          (startDrag [e]
            (proxy-super startDrag e)
            (.setHandled e true)
            (. (. e getPickedNode) moveToFront))
          (drag [e]
            (proxy-super drag e)
            (let [edges (. (. e getPickedNode) getAttribute "edges")]
              (println "edges during drag")
              (println edges)
              (map update-edge edges)
              )))))
    ))

(defn -main []
  (let [window (JFrame.)
        ge (edu.umd.cs.piccolo.PCanvas.GraphEditor. 500 500)]
    (println "... got to beginning of let")
    (doto window
      (.setTitle "Piccolo Graphics Editor")
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE))
    (.add (.getContentPane window) ge)
    (println "... got to adding Graphics Editor to window")
    (doto window
      (.pack)
      (.setVisible true)))
  (println "Goodbye!"))
