
using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace comm {
	public class RioCommProtocol {

		public static void startComm(int teamNum, String fallback) {
			IPAddress resolved = resolveRio(teamNum, fallback);
			if(resolved != null) IOHandler.start(resolved);
		}

		public static void close() {
			IOHandler.close();
		}

		public static SendingPacket getSending() {
			return IOHandler.getSendingPacket();
		}

		public static void setSending(SendingPacket sending) {
			IOHandler.setSendingPacket(sending);
		}

		public static void wantEnabled(bool enabled) {
			getSending().setWantEnabled(enabled);
		}

		public static void wantFMS(bool FMS) {
			getSending().setWantFMS(FMS);
		}

		public static void wantEstop(bool estop) {
			getSending().setWantEstop(estop);
		}

		public static void wantReset(bool reset) {
			getSending().setWantReset(reset);
		}

		public static void wantRestart(bool restart) {
			getSending().setWantRestart(restart);
		}

		public static void setMode(SendingPacket.ControlMode mode) {
			getSending().setMode(mode);
		}

		public static SendingPacket.ControlMode getMode() {
			return getSending().getMode();
		}

		public static RecievingPacket getRecieving() {
			return IOHandler.getRecievingPacket();
		}

		private static IPAddress resolveRio(int teamNum, String fallback) {
			IPAddress[] resolved;
			try {
				resolved = Dns.GetHostAddresses("roborio-" + teamNum + "-frc.local");
				if (resolved.Length > 0) {
					Console.WriteLine("Rio found at " + resolved[0]);
					return resolved[0];
				}
			}
			catch (SocketException ex) {
				Console.WriteLine("RIO not found with DNS");
			}

			try {
				resolved = Dns.GetHostAddresses(fallback);
				if (resolved.Length > 0) {
					Console.WriteLine("Rio found at " + resolved[0]);
					return resolved[0];
				}
			}
			catch (SocketException ex) {
				Console.WriteLine("RIO not found with fallback IP");
			}

			throw new Exception("Could not Resolve with DNS or fallback", new SocketException());
		}

		private class IOHandler {
			private const int sendingPort = 1110;
			private const int recievingPort = 1150;
			private static SendingPacket sending = new SendingPacket();
			private static RecievingPacket recieving = new RecievingPacket(64);
			private static byte[] recieve = new byte[64];
			private static UdpClient udpClient;
			private static IPAddress rioAddress;
			private static IPEndPoint RioWithPort;
			private static bool wantStop = false;

			private static void handleComm() {

				while (!wantStop) {
					try {
						udpClient.Connect(RioWithPort);
						byte[] toSend;
						while (!wantStop) {
							//sending
							toSend = sending.getPacket();
							udpClient.Send(toSend, toSend.Length);

							//wait for response
							Thread.Sleep(18);

							//receive into system
							recieve = udpClient.Receive(ref RioWithPort);
							recieving.setData(recieve);
						}
					}
					catch (Exception e) {
						Console.Out.WriteLine("Encountered an exception while operating com protocol: " + e);
					}
				}

			}

			public static void start(IPAddress address) {
				rioAddress = address;
				RioWithPort = new IPEndPoint(rioAddress, sendingPort);
				udpClient = new UdpClient(recievingPort);
				Thread commThread = new Thread(handleComm);
				commThread.Start();
			}

			public static void close() {
				wantStop = true;
				udpClient?.Close();
			}

			public static void setSendingPacket(SendingPacket packet) {
				sending = packet;
			}

			public static SendingPacket getSendingPacket() {
				return sending;
			}

			public static RecievingPacket getRecievingPacket() {
				return recieving;
			}

		}
	}

}
