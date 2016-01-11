/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
/* EcoSmart RGBW (via Zigbee)

Capabilities:
  Actuator
  Colour Control
  Configuration
  Polling
  Refresh
  Sensor
  Switch
  Switch Level
  Colour Temperature
  
Custom Commands:
  setAdjustedColor
  setGenericName
  
Created using the base code from SmartThings Community:
SmartThingsPublic/devicetypes/smartthings/zigbee-hue-bulb.src/zigbee-hue-bulb.groovy
SmartThingsPublic/devicetypes/smartthings/osram-lightify-led-tunable-white-60w.src/osram-lightify-led-tunable-white-60w.groovy

*/

metadata {
	definition (name: "Zigbee RGBW Temp Bulb", namespace: "snakedog116", author: "Snakedog116") {
		capability "Switch Level"
		capability "Actuator"
		capability "Color Control"
		capability "Switch"
		capability "Configuration"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
        capability "Color Temperature"

		command "setAdjustedColor"
        fingerprint profileId: "0104", inClusters: "0000,0003,0004,0005,0006,0008,0300,0B04", outClusters: "0019"
}

	// simulator metadata
	simulator {
		// status messages
		status "on": "on/off: 1"
		status "off": "on/off: 0"

		// reply messages
		reply "zcl on-off on": "on/off: 1"
		reply "zcl on-off off": "on/off: 0"
	}

	// UI tile definitions
	tiles(scale: 2) {

        multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
            tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
                attributeState "on", label:'${name}', action:"switch.off", icon:"st.switches.light.on", backgroundColor:"#79b821", nextState:"turningOff"
                attributeState "off", label:'${name}', action:"switch.on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
                attributeState "turningOn", label:'${name}', action:"switch.off", icon:"st.switches.light.on", backgroundColor:"#79b821", nextState:"turningOff"
                attributeState "turningOff", label:'${name}', action:"switch.on", icon:"st.switches.light.off", backgroundColor:"#ffffff", nextState:"turningOn"
            }
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
                attributeState "level", action:"switch level.setLevel", backgroundColor:"#79b821"

            }
            tileAttribute ("device.color", key: "COLOR_CONTROL") {
                attributeState "color", action:"setAdjustedColor"
            }
            
            
        }    
		standardTile("switch2", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "on", label:'${name}', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#79b821", nextState:"turningOff"
			state "off", label:'${name}', action:"switch.on", icon:"st.switches.switch.off", backgroundColor:"#ffffff", nextState:"turningOn"
			state "turningOn", label:'${name}', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#79b821", nextState:"turningOff"
			state "turningOff", label:'${name}', action:"switch.on", icon:"st.switches.switch.off", backgroundColor:"#ffffff", nextState:"turningOn"
		}
		standardTile("refresh", "device.switch", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
		controlTile("rgbSelector", "device.color", "color", height: 6, width: 6, inactiveLabel: false) {
			state "color", action:"setAdjustedColor"
		}
		controlTile("levelSliderControl", "device.level", "slider", height: 2, width: 4, inactiveLabel: false, range:"(0..100)") {
			state "level", action:"switch level.setLevel", backgroundColor:"#79b821"
		}
		valueTile("level", "device.level", inactiveLabel: false, height: 2, width: 2, decoration: "flat") {
			state "level", label: 'Level ${currentValue}%',
          	  backgroundColors: [
					[value: 0, color: "#000000"],
                    [value: 10, color: "#243709"],
					[value: 30, color: "#3c5c10"],
					[value: 50, color: "#548017"],
					[value: 70, color: "#6ca51d"],
					[value: 90, color: "#79b821"],
			]
		}
		controlTile("saturationSliderControl", "device.saturation", "slider", height: 2, width: 4, range:"(0..100)", inactiveLabel: false) {
			state "saturation", action:"color control.setSaturation", backgroundColor:"#ff8100"
		}
		valueTile("saturation", "device.saturation", height: 1, width: 2, inactiveLabel: false, decoration: "flat") {
			state "saturation", label: 'Sat ${currentValue}', backgroundColor:"#ff8100"
		}
		controlTile("hueSliderControl", "device.hue", "slider", height: 2, width: 4, range:"(0..100)", inactiveLabel: false) {
			state "hue", action:"color control.setHue", backgroundColor: "#f00"
		}
		valueTile("hue", "device.hue", height: 1, width: 2, inactiveLabel: false, decoration: "flat") {
			state "hue", label: 'Hue ${currentValue}',
          	  backgroundColors: [
					[value: 0, color: "#FF0000"],
                    [value: 10, color: "#FF9900"],
                    [value: 20, color: "#FAE100"],
					[value: 30, color: "#33FF00"],
					[value: 40, color: "#0AFFF7"],
					[value: 50, color: "#05B0FF"],
					[value: 60, color: "#0066FF"],
                    [value: 70, color: "#DE5CFF"],
                    [value: 80, color: "#D738FF"],
                    [value: 90, color: "#FF0099"],
                    [value: 100, color: "#FF0000"],
				]
            }
        controlTile("colorTempSliderControl", "device.colorTemperature", "slider", height: 2, width: 4, inactiveLabel: false, range:"(2700..6500)") {
            state "colorTemperature", action:"color temperature.setColorTemperature", backgroundColor:"#FFA757"
		}
        valueTile("colorTemp", "device.colorTemperature", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
            state "colorTemperature", label: '${currentValue}K',
          	  backgroundColors: [
					[value: 3000, color: "#FFA757"],
                    [value: 3500, color: "#FFC18D"],
					[value: 4500, color: "#FFDABB"],
//					[value: 5500, color: "#FFEDDE"],
					[value: 5500, color: "#bed0ff"],
					[value: 6000, color: "#8babff"],
			]
        }
		main(["switch"])
		details(["switch", "levelSliderControl", "level", "colorTempSliderControl", "colorTemp", "rgbSelector", "hueSliderControl", "hue", "saturation", "saturationSliderControl", "refresh"])
	}
}

// Parse incoming device messages to generate events
def parse(String description) {
	//log.trace description
	if (description?.startsWith("catchall:")) {
		def msg = zigbee.parse(description)
		//log.trace msg
		//log.trace "data: $msg.data"
	}
	else {
		def name = description?.startsWith("on/off: ") ? "switch" : null
		def value = name == "switch" ? (description?.endsWith(" 1") ? "on" : "off") : null
		def result = createEvent(name: name, value: value)
		log.debug "Parse returned ${result?.descriptionText}"
		return result
	}
}
def on() {
	// just assume it works for now
	log.debug "on()"
	sendEvent(name: "switch", value: "on")
	"st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
}

def off() {
	// just assume it works for now
	log.debug "off()"
	sendEvent(name: "switch", value: "off")
	"st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"
}
def setHue(value) {
	def max = 0xfe
	log.trace "setHue($value)"
	sendEvent(name: "hue", value: value)
	def scaledValue = Math.round(value * max / 100.0)
	def cmd = "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x00 {${hex(scaledValue)} 00 0000}"
	//log.info cmd
	cmd
}

def setAdjustedColor(value) {
	log.debug "setAdjustedColor: ${value}"
	def adjusted = value + [:]
	adjusted.hue = adjustOutgoingHue(value.hue)
	adjusted.level = null // needed because color picker always sends 100
	setColor(adjusted)
}

def setColor(value){
	log.trace "setColor($value)"
	def max = 0xfe

	sendEvent(name: "hue", value: value.hue)
	sendEvent(name: "saturation", value: value.saturation)
	def scaledHueValue = Math.round(value.hue * max / 100.0)
	def scaledSatValue = Math.round(value.saturation * max / 100.0)

	def cmd = []
	if (value.switch != "off" && device.latestValue("switch") == "off") {
		cmd << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 1 {}"
		cmd << "delay 150"
	}

	cmd << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x00 {${hex(scaledHueValue)} 00 0000}"
	cmd << "delay 150"
	cmd << "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x03 {${hex(scaledSatValue)} 0000}"

	if (value.level != null) {
		cmd << "delay 150"
		cmd.addAll(setLevel(value.level))
	}

	if (value.switch == "off") {
		cmd << "delay 150"
		cmd << off()
	}
	log.info cmd
	cmd
}

def setSaturation(value) {
	def max = 0xfe
	log.trace "setSaturation($value)"
	sendEvent(name: "saturation", value: value)
	def scaledValue = Math.round(value * max / 100.0)
	def cmd = "st cmd 0x${device.deviceNetworkId} ${endpointId} 0x300 0x03 {${hex(scaledValue)} 0000}"
	//log.info cmd
	cmd
}
//Updated to try to make the refresh button refresh current state.
def refresh() {
    zigbee.onOffRefresh() + zigbee.levelRefresh() + zigbee.colorTemperatureRefresh() + zigbee.onOffConfig() + zigbee.levelConfig() + zigbee.colorTemperatureConfig()//OLD CODE	"st rattr 0x${device.deviceNetworkId} 1 6 0"
}

def poll(){
	log.debug "Poll is calling refresh"
	refresh()
}

def setLevel(value) {
	log.trace "setLevel($value)"
	def cmds = []

	if (value == 0) {
		sendEvent(name: "switch", value: "off")
		cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 6 0 {}"
	}
	else if (device.latestValue("switch") == "off") {
		sendEvent(name: "switch", value: "on")
	}

	sendEvent(name: "level", value: value)
    def level = hexString(Math.round(value * 255/100))
	cmds << "st cmd 0x${device.deviceNetworkId} ${endpointId} 8 4 {${level} 0000}"
	cmds
}

def setColorTemperature(value) {
	sendEvent(name: "colorTemperature", value: value)
    zigbee.setColorTemperature(value)
}

//This should allow real-time updating if the physical switch is turned on or off.
def configure() {
    log.debug "Configuring Reporting and Bindings."
    zigbee.onOffRefresh() + zigbee.levelRefresh() + zigbee.colorTemperatureRefresh() + zigbee.onOffConfig() + zigbee.levelConfig() + zigbee.colorTemperatureConfig()//OLD CODE	"st rattr 0x${device.deviceNetworkId} 1 6 0"
}

private getEndpointId() {
	new BigInteger(device.endpointId, 16).toString()
}

private hex(value, width=2) {
	def s = new BigInteger(Math.round(value).toString()).toString(16)
	while (s.size() < width) {
		s = "0" + s
	}
	s
}

private adjustOutgoingHue(percent) {
	def adjusted = percent
	if (percent > 31) {
		if (percent < 63.0) {
			adjusted = percent + (7 * (percent -30 ) / 32)
		}
		else if (percent < 73.0) {
			adjusted = 69 + (5 * (percent - 62) / 10)
		}
		else {
			adjusted = percent + (2 * (100 - percent) / 28)
		}
	}
	log.info "percent: $percent, adjusted: $adjusted"
	adjusted
}