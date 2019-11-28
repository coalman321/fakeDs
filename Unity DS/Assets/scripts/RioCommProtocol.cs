
using System;
using System.Net;
using System.Net.Sockets;
using System.Threading;

namespace comm {
	public class RioCommProtocol {

		public static void startComm(int teamNum, String fallback) {
			IPAddress resolved = resolveRio(teamNum, fallback);
			IOHandler.start(resolved);
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

			resolved = Dns.GetHostAddresses("roborio-" + teamNum + "-frc.local");
			if (resolved.Length > 0) {
				Console.Out.WriteLine("Rio found at " + resolved[0]);
				return resolved[0];
			}

			resolved = Dns.GetHostAddresses(fallback);
			if (resolved.Length > 0) {
				Console.Out.WriteLine("Rio found at " + resolved[0]);
				return resolved[0];
			}

			throw new SocketException();
		}

		private class IOHandler {
			private const int sendingPort = 1110;
			private const int recievingPort = 1150;
			private static SendingPacket sending = new SendingPacket();
			private static RecievingPacket recieving;
			private static byte[] recieve;
			private static UdpClient udpClient;
			private static IPAddress rioAddress;
			private static IPEndPoint RioWithPort;
			private static bool wantStop = false;

			private static void handleComm() {

				while (!wantStop) {
					try {
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
				recieve = new byte[64];
				recieving = new RecievingPacket(64);
				RioWithPort = new IPEndPoint(rioAddress, sendingPort);
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
