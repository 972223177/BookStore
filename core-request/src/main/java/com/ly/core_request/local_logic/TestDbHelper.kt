package com.ly.core_request.local_logic

import com.ly.core_db.bean.*
import com.ly.core_db.helpers.BookDbHelper
import com.ly.core_db.helpers.UserDbHelper

object TestDbHelper {

    suspend fun initDb() {
        addCategories()
        addBooks()
        addUsers()
    }

    private suspend fun addCategories() {
        val categories = listOf(
            BookCategory(
                1, "小说", System.currentTimeMillis(), System.currentTimeMillis()
            ), BookCategory(
                2, "影视原著", System.currentTimeMillis(), System.currentTimeMillis()
            ), BookCategory(
                3, "心理学", System.currentTimeMillis(), System.currentTimeMillis()
            ), BookCategory(
                4, "历史", System.currentTimeMillis(), System.currentTimeMillis()
            ), BookCategory(
                5, "互联网", System.currentTimeMillis(), System.currentTimeMillis()
            ), BookCategory(
                6, "期刊杂志", System.currentTimeMillis(), System.currentTimeMillis()
            ), BookCategory(
                7, "管理学", System.currentTimeMillis(), System.currentTimeMillis()
            ),
            BookCategory(
                8, "计算器", System.currentTimeMillis(), System.currentTimeMillis()
            )
        )
        BookDbHelper.addCategories(categories)
    }

    private suspend fun addBooks() {
        initBook1()
        initBook2()
        initBook3()
    }

    private suspend fun addUsers() {
        val user1 = User(
            id = 1,
            name = "好孩子哈哈哈",
            account = "123",
            avatar = "https://img1.baidu.com/it/u=4231629362,98750385&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665680400&t=b4c0ef181d0141c7d88a2bb4ba041370",
            pwd = "123",
            recentReadBookId = 2,
            createTime = System.currentTimeMillis()
        )
        if (UserDbHelper.add(user1)) {
            val userBook = UserBook(
                id = 1,
                ownerId = 1,
                bookId = 3,
                bookName = "红楼梦（中国古典文学读本丛书）",
                readChapterId = 2,
                readChapterIndex = 2,
                readChapterName = "这是第2章",
                isPurchased = true,
                readProgress = 2 / 123.0,
                createTime = System.currentTimeMillis(),
                updateTime = System.currentTimeMillis()
            )
            UserDbHelper.addUserBook(userBook)
        }
        val user2 = User(
            id = 2,
            name = "尽快了解大端",
            account = "1234",
            avatar = "https://img2.baidu.com/it/u=3728042108,3864890977&fm=253&app=138&size=w931&n=0&f=JPEG&fmt=auto?sec=1665680400&t=e19059e40ddb929ff945d351db3e1992",
            pwd = "1234",
            recentReadBookId = 2,
            createTime = System.currentTimeMillis()
        )
        if (UserDbHelper.add(user2)) {
            val userBook = UserBook(
                id = 2,
                ownerId = 2,
                bookId = 2,
                bookName = "两京十五日",
                readChapterId = 2,
                readChapterIndex = 2,
                readChapterName = "这是第2章",
                isPurchased = true,
                readProgress = 2 / 32.0,
                createTime = System.currentTimeMillis(),
                updateTime = System.currentTimeMillis()
            )
            UserDbHelper.addUserBook(userBook)
        }
        val followInfo1 = UserFollow(
            id = 1,
            followId = 1,
            userId = 2,
            createTime = System.currentTimeMillis(),
            updateTime = System.currentTimeMillis()
        )
        UserDbHelper.addUserFollow(followInfo1)
        val followInfo2 = UserFollow(
            id = 2,
            followId = 2,
            userId = 1,
            createTime = System.currentTimeMillis(),
            updateTime = System.currentTimeMillis()
        )
        UserDbHelper.addUserFollow(followInfo2)
    }

    private suspend fun initBook1() {
        val book1 = Book(
            id = 1,
            categoryId = 1,
            name = "我妈让我卖茄子",
            des = "这是一个发生在日本上个世纪七十年代的真实故事。我那温柔的妈妈，有一天突然变得像鬼一样厉害，让我一个人去卖茄子。这一切究竟是为什么呢？四十年前发生在日本乡村小镇的故事，今天仍可以感动千万人。一本会让你流泪的绘本，一本冲击力十足的话题之作。作者想要传达给大家的就是：请把孩子培养成一个坚强的人。在越来越多的年轻人被称为“巨婴”“妈宝”的时代，这个过去的故事，会带给我们和孩子深刻的启示。希望所有大人和父母，读一读这本催人泪下的图画书。",
            price = 9.9,
            cover = "https://piccdn3.umiwi.com/img/201906/21/201906211724358663175638.jpg?x-oss-process=image/resize,m_fill,h_320,w_240",
            chapterCount = 18,
            author = "原田刚",
            color = 0xffE6E367,
            createTime = System.currentTimeMillis()
        )
        if (BookDbHelper.addBook(book1)) {
            addChapters(18, 1)
        }
    }

    private suspend fun initBook2() {
        val book2 = Book(
            id = 2,
            categoryId = 2,
            name = "两京十五日",
            des = "《两京十五日》是马伯庸创作的一本长篇历史小说。本书故事源于《明史》里关于朱瞻基的一段真实记载——“夏四月，以南京地屡震，命往居守。五月庚辰，仁宗不豫，玺书召还。六月辛丑，还至良乡，受遗诏，入宫发丧。”\n" +
                    "\n" +
                    "史书中的寥寥几字，背后究竟隐藏着怎样的深意？匆匆数句记载，谁才是真正的书写者？\n" +
                    "\n" +
                    "千里长河，星夜奔驰，四面楚歌，命悬一线。太子这一场沿着大运河的极速奔跑，跑出了皇权与民意的博弈，跑出了宫闱与官场的心机搏杀，跑出了纠葛数十年的复杂恩怨，也跑出了从崇高到卑贱的幽微人心。\n" +
                    "\n" +
                    "这是一个小捕快、一个女医生、一个芝麻官和一个当朝太子的心灵之旅，一幅描绘明代大运河沿岸风情的鲜活画卷。",
            price = 56.0,
            color = 0xffD9AF4D,
            cover = "https://piccdn3.umiwi.com/img/202011/03/202011031421267654744190.jpg?x-oss-process=image/resize,m_fill,h_320,w_240",
            chapterCount = 32,
            author = "马伯庸",
            createTime = System.currentTimeMillis()
        )
        if (BookDbHelper.addBook(book2)) {
            addChapters(32, 2)
        }
    }

    private suspend fun initBook3() {
        val book3 = Book(
            id = 3,
            categoryId = 2,
            name = "红楼梦（中国古典文学读本丛书）",
            des = "《红楼梦》以宝黛爱情悲剧为主线，以四大家族的荣辱兴衰为背景，描绘出18世纪中国封建社会的方方面面，以及封建专制下新兴资本主义民主思想的萌动。结构宏大、情节委婉、细节精致，人物形象栩栩如生，声口毕现，堪称中国古代小说中的经典。\n" +
                    "\n" +
                    "由红楼梦研究所校注、人民文学出版社出版的《红楼梦》以庚辰（1760）本《脂砚斋重评石头记》为底本，以甲戌（1754）本、已卯（1759）本、蒙古王府本、戚蓼生序本、舒元炜序本、郑振铎藏本、红楼梦稿本、列宁格勒藏本（俄藏本）、程甲本、程乙本等众多版本为参校本，是一个博采众长、非常适合大众阅读的本子；同时，对底本的重要修改，皆出校记，读者可因以了解《红楼梦》的不同版本状况。\n" +
                    "\n" +
                    "红学所的校注本已印行二十五年，其间1994年曾做过一次修订，又十几年过去，2008年推出修订第三版，体现了新的校注成果和科研成果。\n" +
                    "\n" +
                    "关于《红楼梦》的作者，原本就有多种说法及推想，“前八十回曹雪芹著、后四十回高鹗续”的说法只是其中之一，这次修订中校注者改为“前八十回曹雪芹著；后四十回无名氏续，程伟元、高鹗整理”，应当是一种更科学的表述，体现了校注者对这一问题的新的认识。现在这个修订后的《红楼梦》更加完善。",
            price = 22.99,
            color = 0xff817CB7,
            cover = "https://piccdn3.umiwi.com/img/202004/04/202004042318184241563840.jpg?x-oss-process=image/resize,m_fill,h_320,w_240",
            chapterCount = 123,
            author = "曹雪芹",
            createTime = System.currentTimeMillis()
        )
        if (BookDbHelper.addBook(book3)) {
            addChapters(123, 3)
        }
    }


    private suspend fun addChapters(count: Int, bookId: Int) {
        val chapters = List(count) {
            var content = ""
            for (i in 0..100) {
                if (i % 20 == 0) {
                    content += "\n  段落开头,"
                }
                content += "这是内容啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊。"
            }
            BookChapter(
                id = it + 1,
                bookId = bookId,
                index = it + 1,
                name = "这是第${it}章",
                content = content,
                createTime = System.currentTimeMillis(),
                updateTime = System.currentTimeMillis()
            )
        }
        BookDbHelper.addChapters(chapters)
    }

}