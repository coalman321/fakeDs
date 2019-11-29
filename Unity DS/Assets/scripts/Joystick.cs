
using System;
using System.Collections.Generic;

namespace comm {
	public class Joystick : SendingPacket.Tag {
		private byte[] axes;
		private byte[] buttons;
		
		public Joystick(int numAxes, int numButtons) {
			id = SendingPacket.TagID.JOYSTICK;
			buttons = new byte[(int)Math.Ceiling(numButtons/8.0)];
			axes = new byte[numAxes];
		}

		public void setAxis(int axisNum, sbyte val) {
			if (axisNum < axes.Length) axes[axisNum] = (byte)val;
		}

		public void setButton(int button, bool isPressed) {
			
		}

		public override byte[] getPayload() {
			List<byte> byteList = new List<byte>();
			byteList.Add((byte)id);
			byteList.Add((byte)axes.Length);
			byteList.AddRange(axes);
			//byteList.Add((byte)Math.Ceiling(buttons.Length/8.0));
			byteList.Add(0x02);
			byteList.Add(0x00);
			byteList.Add(0x00);
			//add POV data later
			byteList.Insert(0, (byte)byteList.Count);//add tag size to start of packet
			return byteList.ToArray();
		}
	}
}