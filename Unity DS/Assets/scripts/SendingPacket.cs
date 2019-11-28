﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;


namespace comm {
	public class SendingPacket {
	
		private ushort counter = 0;
		private bool wantReset, wantRestart, wantFMS, wantEstop, wantEnabled;
		private ControlMode mode;
		private AllianceStation station;
		private List<Tag> tags;
		
		public SendingPacket(bool reset, bool restart, bool fms,
			bool estop, bool enabled, ControlMode mode, AllianceStation station){
			wantReset = reset; wantRestart = restart; wantFMS = fms;
			wantEstop = estop; wantEnabled = enabled; this.mode = mode;
			this.station = station;
		}
		public SendingPacket(){
			wantReset = false; wantRestart = false; wantFMS = false;
			wantEstop = false; wantEnabled = false; mode = ControlMode.TELEOP;
			station = AllianceStation.RED1;
		}
		
		public void setPacket(bool reset, bool restart, bool fms,
			bool estop, bool enabled, ControlMode mode, AllianceStation station){
			wantReset = reset; wantRestart = restart; wantFMS = fms;
			wantEstop = estop; wantEnabled = enabled; this.mode = mode;
			this.station = station;
		}
		
		public byte[] getPacket() {
			counter++;
	
			byte control = (byte)(wantEstop ? 0x80 : 0x00);
			control |= (byte)(wantFMS ? 0x08 : 0x00);
			control |= (byte)(wantEnabled ? 0x04 : 0x00);
			control |= (byte)mode;
	
			byte request = (byte)(wantRestart ? 0x08 : 0x00);
			request |= (byte)(wantReset ? 0x04 : 0x00);
	
			return new byte[] {(byte) (counter >> 8), (byte) (counter & 0xff), 0x01, control, request, (byte)station};
		}
		
		public bool isWantReset(){
			return wantReset;
		}
	
		public void setWantReset(bool wantReset) {
			this.wantReset = wantReset;
		}
	
		public bool isWantRestart() {
			return wantRestart;
		}
	
		public void setWantRestart(bool wantRestart) {
			this.wantRestart = wantRestart;
		}
	
		public bool isWantFMS() {
			return wantFMS;
		}
	
		public void setWantFMS(bool wantFMS) {
			this.wantFMS = wantFMS;
		}
	
		public bool isWantEstop() {
			return wantEstop;
		}
	
		public void setWantEstop(bool wantEstop) {
			this.wantEstop = wantEstop;
		}
	
		public bool isWantEnabled() {
			return wantEnabled;
		}
	
		public void setWantEnabled(bool wantEnabled) {
			this.wantEnabled = wantEnabled;
		}
		
		public AllianceStation getStation() {
			return station;
		}
	
		public void setStation(AllianceStation station) {
			this.station = station;
		}
	
		public ControlMode getMode() {
			return mode;
		}
	
		public void setMode(ControlMode mode) {
			this.mode = mode;
		}
		
		public enum ControlMode: byte{
			AUTO = 2,
			TEST = 1,
			TELEOP = 0
		}
	
		public enum AllianceStation: byte{
			RED1 = 0,
			RED2 = 1,
			RED3 = 2,
			BLUE1 = 3,
			BLUE2 = 4,
			BLUE3 = 5
		}
	
	
		public class Tag{
			private byte size;
			private ID id;
			byte[] payload;
			
			public Tag(ID id, byte[] payload){
			
			}
		
		}
	
		public enum ID: byte {
			COUNTDOWN = 0x07,
			JOYSTICK = 0x0c,
			DATE = 0x0f,
			TIMEZONE = 0x01
		}
	}
}

