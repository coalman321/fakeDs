package riocomm2;

import java.net.*;

public class RioCommProtocol {

    InetAddress mRioAddress;
    IOHandler ioHandler;

    public RioCommProtocol(int teamnum, String fallbackAddress) throws UnknownHostException{
        mRioAddress = getAddress(teamnum, fallbackAddress);
        ioHandler = new IOHandler();
    }

    public void start() throws Exception{
        ioHandler.start(mRioAddress);
        if(ioHandler.getLastException() != null)
            throw ioHandler.getLastException();
    }

    public Exception getExceptions(){
        return ioHandler.getLastException();
    }

    public void close(){
        ioHandler.close();
    }

    public SendingPacket getSending(){
        return ioHandler.getSendingPacket();
    }

    public void setSending(SendingPacket sending){
        ioHandler.setSendingPacket(sending);
    }

    public void wantEnabled(boolean enabled){
        getSending().setWantEnabled(enabled);
    }

    public void wantFMS(boolean FMS){
        getSending().setWantFMS(FMS);
    }

    public void wantEstop(boolean estop){
        getSending().setWantEstop(estop);
    }

    public void wantReset(boolean reset){
        getSending().setWantReset(reset);
    }

    public void wantRestart(boolean restart){
        getSending().setWantRestart(restart);
    }

    public void setMode(SendingPacket.ControlMode mode){
        getSending().setMode(mode);
    }

    public SendingPacket.ControlMode getMode(){
        return getSending().getMode();
    }

    public RecievingPacket getRecieving(){
        return ioHandler.getRecievingPacket();
    }

    private InetAddress getAddress(int teamNum, String fallback) throws UnknownHostException {
        InetAddress rioAddress = null;
        try {
            rioAddress = InetAddress.getByName("roborio-" + teamNum + "-frc.local");
            System.out.println("Rio found at: " + rioAddress.getHostAddress());
            return rioAddress;
        } catch (UnknownHostException e) {
            System.out.println("System Error: could not find RIO with hostname '" + e.getMessage() + "'");
            try {
                rioAddress = InetAddress.getByName(fallback);
                System.out.println("Rio found at: " + rioAddress.getHostAddress());
                return rioAddress;
            } catch (UnknownHostException except1) {
                System.out.println("System Error: could not find RIO with address '" + e.getMessage() + "'");
            }
        }
        throw new UnknownHostException();
    }

    private class IOHandler{

        private static final int sendingPort = 1110;
        private static final int recievingPort = 1150;
        private InetAddress mToRio;
        private boolean wantStop = false;
        private Exception mLastException = null;
        private DatagramSocket sock;
        private Thread runner;
        private SendingPacket sending = SendingPacket.defaultPacket;
        private RecievingPacket recieving;
        private byte[] recieve;

        private final Runnable run = () -> {
            while(!isValidError() && !wantStop) {
                try {
                    sock = new DatagramSocket(recievingPort);
                    sock.setSoTimeout(10);
                    byte[] toSend;
                    while (!wantStop) {
                        //sending
                        toSend = sending.getPacket();
                        DatagramPacket toRio = new DatagramPacket(toSend, toSend.length, mToRio, sendingPort);
                        sock.send(toRio);

                        //wait for response
                        Thread.sleep(18);

                        //receive into system
                        DatagramPacket fromRio = new DatagramPacket(recieve, recieve.length);
                        sock.receive(fromRio);
                        recieving.setData(fromRio.getData());


                    }
                } catch (Exception e) {
                    mLastException = e;
                    sock.close();
                }
            }
        };


        public void start(InetAddress address){
            mToRio = address;
            recieve = new byte[64];
            recieving = new RecievingPacket(64);
            runner = new Thread(run);
            runner.start();
        }

        public void close(){
            wantStop = true;
            if(sock != null) sock.close();
        }

        public void setSendingPacket(SendingPacket packet){
            sending = packet;
        }

        public SendingPacket getSendingPacket(){
            return sending;
        }

        public RecievingPacket getRecievingPacket(){
            return recieving;
        }

        public Exception getLastException() {
            if(isValidError()) {
                Exception prep = mLastException;
                mLastException = null;
                return prep;
            }
            return null;
        }

        public boolean getStopped(){
            return wantStop;
        }

        private boolean isValidError(){
            return mLastException != null &&
                    !((mLastException instanceof InterruptedException) ||
                    (mLastException instanceof SocketTimeoutException));
        }

    }

}
