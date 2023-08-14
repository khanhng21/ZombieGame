package ZombiesGame.controller;

import ZombiesGame.messages.Message;

/**
 * The Valve interface is implemented by all the inner classes in the Controller class in order to execute their respective functions
 * depending on the Message object passed
 */
public interface Valve {
    /**
     * This function can call certain functions from classes in the model and view packages. This is
     * inherited by the Valves in the Controller class.
     * @param message the message passed from the view and received by Valves in the Controller.
     * A valve may ignore the message or execute the message depending on what message the Valve is looking for
     * @return a return value whether the valve response is either ignored or executed
     */
    public ValveResponse execute(Message message);
}
