package com.example.carexplorer.helpers


//object XMLHelper {
//
//    suspend fun fetchImage(url: String): List<String> {
//        val doc = Jsoup.connect(url).ignoreHttpErrors(true).timeout(30000)
//        val sourceXML = doc.get().toString()
//        val builder: DocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
//        val src = InputSource()
//        src.characterStream = StringReader(sourceXML)
//
//        val parsedString = builder.parse(src)
//
//        val itemsXML = parsedString.documentElement.getElementsByTagName("item")
//        val listUrls: MutableList<String> = mutableListOf()
//        var hasEnclosure = false
//        for (i in 0 until itemsXML.length) {
//            val childs = itemsXML.item(i).childNodes
//            for (j in 0 until childs.length) {
//                if (childs.item(j).nodeName == "enclosure") {
//                    val child = childs.item(j).attributes
//                    val itemUrl = child.getNamedItem("url")
//                    if (itemUrl.nodeValue.contains("/jpg")
//                        || itemUrl.nodeValue.contains("/png")
//                        || itemUrl.nodeValue.contains(".png")
//                        || itemUrl.nodeValue.contains(".jpg")
//                        || itemUrl.nodeValue.contains(".jpeg")
//                        || itemUrl.nodeValue.contains(".jpeg")
//                    ) {
//                        listUrls.add(itemUrl.nodeValue)
//                        hasEnclosure = true
//                        break
//                    }
//                }
//            }
//            if (!hasEnclosure) {
//                listUrls.add("Nothing")
//                hasEnclosure = false
//            }
//        }
//        return listUrls
//    }
//}

