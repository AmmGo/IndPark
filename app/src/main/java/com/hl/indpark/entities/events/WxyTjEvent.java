package com.hl.indpark.entities.events;

public class WxyTjEvent {

    public int key;
    public int value;
//    {
//        "code": 200,
//            "msg": "success",
//            "data": [
//        {
//            "key": "0",
//                "value": 243
//        },
//        {
//            "key": "1",
//                "value": 2
//        },
//        {
//            "key": "2",
//                "value": 4
//        },
//        {
//            "key": "3",
//                "value": 6
//        },
//        {
//            "key": "4",
//                "value": 8
//        }
//  ]
//    }
    public static String getType(int type) {
        String typeStr = "";
        switch (type) {
            case 1:
                typeStr = "高高报";
                break;
            case 2:
                typeStr = "高报";
                break;
            case 3:
                typeStr = "低报";
                break;
            case 4:
                typeStr = "低低报";
                break;
            default:
        }
        return typeStr;
    }
}
