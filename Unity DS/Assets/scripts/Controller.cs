using comm;
using UnityEngine;


public class Controller : MonoBehaviour {

    private RecievingPacket lastRecievingPacket;
    private SendingPacket lastSendingPacket;
    private Joystick joy = new Joystick(4, 12);
    
    // Start is called before the first frame update
    void Start()
    {
        ConsoleRedirect.Redirect();
        RioCommProtocol.startComm(4145, "10.41.45.2");
        lastSendingPacket = RioCommProtocol.getSending();
        lastSendingPacket.addJoystick(ref joy);
        lastRecievingPacket = RioCommProtocol.getRecieving();
    }

    // Update is called once per frame
    void Update() {
        RioCommProtocol.setSending(lastSendingPacket);
        lastRecievingPacket = RioCommProtocol.getRecieving();
    }

    void OnApplicationQuit() {
        RioCommProtocol.close();
    }

    public void setRobotMode(int mode) {
        lastSendingPacket.setMode((SendingPacket.ControlMode)mode);
    }

    public void setEnabled(bool enable) {
        lastSendingPacket.setWantEnabled(enable);
    }

    public string getRobotData() {
        return lastRecievingPacket.ToString();
    }

    public void updateStick(double x, double y, double z) {
        joy.setAxis(0, clamp((sbyte)(x * 127), -127, 127));
        joy.setAxis(1, clamp((sbyte)(y * 127), -127, 127));
        joy.setAxis(2, clamp((sbyte)(z * 127), -127, 127));
        joy.setAxis(3, 0);
    }

    public static sbyte clamp(sbyte val, sbyte min, sbyte max) {
        if (val > max) return max;
        return val < min ? min : val;
    }
}
