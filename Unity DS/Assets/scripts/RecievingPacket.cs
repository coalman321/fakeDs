
using System.Globalization;

namespace comm {
	public class RecievingPacket {
		byte[] stored;
		int lastCount = 0, currentCount, dropped;

		public RecievingPacket(byte[] recived){
			setData(recived);
		}

		public RecievingPacket(int len){
			setData(new byte[len]);
		}

		public void setData(byte[] data){
			stored = data;
			currentCount = bytesToInt2(stored[1], stored[0]);
			if(lastCount < currentCount - 1)
				dropped += currentCount -1 - lastCount;
			lastCount = currentCount;
		}

		public int getCount(){
			return currentCount;
		}

		public int getDropped(){
			return dropped;
		}

		public double getVoltage(){
			return bytesToInt2(stored[6], stored[5]) / 256.0;
		}

		public override string ToString() {
			return "packet count: " + getCount() + "    Voltage: " + getVoltage().ToString("F2", CultureInfo.InvariantCulture) + "	Dropped: " + getDropped();
		}
	
		public static int bytesToInt2 (byte b1, byte b2)      // unsigned
		{
			return (b2 & 0xFF) << 8 | (b1 & 0xFF);
		}
	}
}


