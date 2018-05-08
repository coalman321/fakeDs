public class RioCommProtocol {

    DSUDPToRIO toRIO;
    DSUDPFromRIO fromRIO;

    public RioCommProtocol(){
        toRIO = new DSUDPToRIO();
        fromRIO = new DSUDPFromRIO();
    }

    public void packetToSend(ToRIOPacket packet){

    }

}
