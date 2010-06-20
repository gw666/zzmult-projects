; file: infocard002.clj
; last changed: 6//10

; HISTORY:

; 6//10:
; GOAL:

; IMPLEMENTATION:

; STATUS:



(ns test.infocard002
  (:gen-class)
  (:import (edu.umd.cs.piccolo PCanvas PNode PLayer)
    (edu.umd.cs.piccolo.nodes   PPath PText)
    (edu.umd.cs.piccolo.event   PBasicInputEventHandler PDragEventHandler
      PDragSequenceEventHandler PInputEvent PInputEventFilter PPanEventHandler
      PZoomEventHandler)
    (edu.umd.cs.piccolo.util   PBounds)
    (edu.umd.cs.piccolox PFrame)
    (edu.umd.cs.piccolox.nodes PClip)
    (java.awt.geom   Dimension2D Point2D)
    (java.awt   BasicStroke Font GraphicsEnvironment Rectangle)
    ))

(defn create-frame
  "Creates the main PFrame used by the program."
  []
  (proxy [PFrame] []
    (initialize []
      ; sample infocard
      (let [t (str "Slow down your day\n\n"
                "We are at our least effective when we act in reaction to \n"
                "whatever was the most recent thought in our head. When the \n"
                "brain is very active, it spins from idea to idea with little\n"
                "sense of connection between the two. Calming the mind becomes \n"
                "necessary before we can hope to have any sense of mastery \n"
                "over how we spend our time."
                )
            cardText (PText. t)
            thisLayer (.. this getCanvas getLayer)

            ; how to create a new font
            font2 (Font. "Monospaced", Font/PLAIN, 14)
            ]
        (.setFont cardText font2)

        ; set cardBorder to enclose cardText node
        (let [cardTextBounds (.getGlobalBounds cardText)]
          (.inset cardTextBounds -14 -14)  ; give it some border space
          (let [cardBorder (PPath. cardTextBounds)
                grabber (PPath/createRectangle 86 30 548 6)]

            ; set hierarchy of layer to border and text nodes
            ;(.setStroke cardBorder (BasicStroke. (float 2)))
            (.addChild thisLayer grabber)
            (.addChild grabber cardBorder)
            (.addChild cardBorder cardText)
            (.animateToPositionScaleRotation cardBorder
              100 50 1 0 0)
            
          (println "grabberBounds = " (.toString (.getGlobalBounds grabber)))
          (println "cardBorderBounds = "
            (.toString (.getGlobalBounds cardBorder)))

            ; ensure that border, and text move together
            (.setChildrenPickable grabber false)

            ; install basic drag event handler
            (.. this getCanvas (addInputEventListener (PDragEventHandler.)))

            ; without next line, mouse drag on one node will cause movement of node
            ; plus pan of canvas, leading to illusion two nodes are linked
            ; by some scaling factor
            (.. this getCanvas (setPanEventHandler nil))
            ))))))

(defn -main []
  (let [main-frame (create-frame)]
    (.setVisible main-frame true)))
