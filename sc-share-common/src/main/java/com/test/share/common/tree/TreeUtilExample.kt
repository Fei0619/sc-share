package com.test.share.common.tree

import com.test.share.common.json.JsonUtils

/**
 * @author 费世程
 * @date 2020/11/30 14:39
 */

@TreeTag(nodeParam = "areaId", parentNodeParam = "parentAreaId", childListParam = "childList")
internal class AreaDto {

  var areaId: Int? = null
  var areaName: String? = null
  var parentAreaId: Int? = null
  var childList: List<AreaDto>? = null

}

fun main() {

  AreaDto::class.java.methods

  val areaDto1 = AreaDto().apply { areaId = 1;areaName = "安徽省";parentAreaId = -1;childList = null; }
  val areaDto2 = AreaDto().apply { areaId = 2;areaName = "马鞍山";parentAreaId = 1;childList = null; }
  val areaDto3 = AreaDto().apply { areaId = 3;areaName = "江苏省";parentAreaId = -1;childList = null; }
  val areaDto4 = AreaDto().apply { areaId = 4;areaName = "南京";parentAreaId = 3;childList = null; }
  val areaDto5 = AreaDto().apply { areaId = 5;areaName = "苏州";parentAreaId = 3;childList = null; }
  val areaDto6 = AreaDto().apply { areaId = 6;areaName = "建邺区";parentAreaId = 4;childList = null; }

  val areaList = listOf(areaDto1, areaDto2, areaDto3, areaDto4, areaDto5, areaDto6)
  val treeList = areaList.toTreeArray()
  System.err.println(JsonUtils.toJsonString(treeList))

}
