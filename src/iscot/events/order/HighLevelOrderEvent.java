package iscot.events.order;

import iscot.enums.Areas;
import iscot.events.Event;
import iscot.enums.Type;

    public abstract class HighLevelOrderEvent extends Event {
        private static final long serialVersionUID = 1L;
        
        public HighLevelOrderEvent(Areas area){
            super(Type.DECIDER, area);
        }
    }
