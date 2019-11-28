using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class UpdateRobotData : MonoBehaviour {
    public GameObject parentController;
    private Controller instance;

    void Start() {
        instance = parentController.GetComponent<Controller>();
    }

    // Update is called once per frame
    void Update() {
        gameObject.GetComponent<UnityEngine.UI.Text>().text = instance.getRobotData();
    }
}
