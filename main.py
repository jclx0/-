import jieba
import codecs

storeData=[]
storeDate=[]
content12=[]
content1=[]
content2=[]
content3=[]
content4=[]
content5=[]
content6=[]
currentDate=''

def feelingCal(seperated):
    allWord = []  # 存储的是情感词的总数
    leguan = []
    lengmo = []
    buman = []
    houhui = []
    shiwang = []
    huaiyi = []
    fennu = []
    danxin = []
    gaoxin = []
    zanshang = []
    optimistic = open('情感映射\乐观.txt', 'r', encoding='UTF-8').readlines()
    cold = open('情感映射\冷漠.txt', 'r', encoding='UTF-8').readlines()
    disatisfied = open('情感映射\不满.txt', 'r', encoding='UTF-8').readlines()
    regret = open('情感映射\后悔.txt', 'r', encoding='UTF-8').readlines()
    hopeless = open('情感映射\失望.txt', 'r', encoding='UTF-8').readlines()
    doubt = open('情感映射\怀疑.txt', 'r', encoding='UTF-8').readlines()
    angry = open('情感映射\愤怒.txt', 'r', encoding='UTF-8').readlines()
    worry = open('情感映射\担心.txt', 'r', encoding='UTF-8').readlines()
    happy = open('情感映射\高兴.txt', 'r', encoding='UTF-8').readlines()
    praise = open('情感映射\赞赏.txt', 'r', encoding='UTF-8').readlines()
    for sentense in seperated:
        for word in sentense:
            if word + ' \n' in optimistic:
                allWord.append(word)
                leguan.append(word)
            elif word + ' \n' in cold:
                allWord.append(word)
                lengmo.append(word)
            elif word + " \n" in disatisfied:
                allWord.append(word)
                buman.append(word)
            elif word + " \n" in regret:
                allWord.append(word)
                houhui.append(word)
            elif word + " \n" in hopeless:
                allWord.append(word)
                shiwang.append(word)
            elif word + " \n" in doubt:
                allWord.append(word)
                huaiyi.append(word)
            elif word + " \n" in angry:
                allWord.append(word)
                fennu.append(word)
            elif word + " \n" in worry:
                allWord.append(word)
                danxin.append(word)
            elif word + " \n" in happy:
                allWord.append(word)
                gaoxin.append(word)
            elif word + " \n" in praise:
                allWord.append(word)
                zanshang.append(word)

    total = len(allWord)
    optimisticPer = round(len(leguan) / total,3)
    happyPer = round(len(gaoxin) / total,3)
    praisePer = round(len(zanshang) / total,3)
    worryPer = round(len(danxin) / total,3)
    regretPer = round(len(houhui) / total,3)
    doubtPer = round(len(huaiyi) / total,3)
    disatisfiedPer = round(len(buman) / total,3)
    angryPer = round(len(fennu) / total,3)
    hopelessPer = round(len(shiwang) / total,3)
    coldPer = round(len(lengmo) / total,3)
    active = round(optimisticPer + happyPer + praisePer,3)
    negative=round(worryPer+regretPer+doubtPer+disatisfiedPer+angryPer+hopelessPer+coldPer,3)
    res="active:"+str(active)+" nagetive:"+' optimistic:'+str(optimisticPer)+str(negative)+" happy:"+str(happyPer)+" praise:"+str(praisePer)+" worry:"\
        +str(worryPer)+" regret:"+str(regretPer)+" doubt:"+str(doubtPer)+" disatisfied:"+str(disatisfiedPer)+" angry:"\
        +str(angryPer)+" hopeless:"+str(hopelessPer)+" cold:"+str(coldPer)
    return res

def sent2word(sentence):
    """
    Segment a sentence to words
    Delete stopwords
    """
    segList = jieba.cut(sentence)
    segResult = []
    for w in segList:
        segResult.append(w)
    f=codecs.open('停用词\cn_stopwords.txt','r',encoding='UTF-8')
    stopwords = f.readlines()
    newSent = []
    for word in segResult:
        #allWord.append(word)
        if word+'\n' in stopwords:
            # print "stopword: %s" % word
            continue
        else:
            newSent.append(word)
    return newSent



def analize(text):#先把新闻内容加到对应月份里面
    for i in range (0,len(text),6):
        perSentense=text[i+3]
        SDate=text[i+4]
        seperated = sent2word(perSentense[3:])#新闻内容去停用词
        date=getDate(SDate)
        if storeDate.count(date):
            i=storeDate.index(date)
            storeData[i].append(seperated)
        else:
            storeDate.insert(0,date)
            newData=[]
            storeData.insert(0,newData)
            i=storeDate.index(date)
            storeData[i].append(seperated)

def getDate(SDate):
    if SDate.find('年')!=-1:
        date=SDate[3:7]+'-'+SDate[8:10]+'-'+SDate[11:13]
    else:
        date='2020-06-15'
    return date

def main():
    storeRes=open("情感分析结果\结果.txt",'w+',encoding='UTF-8')
    text1 = open('数据\疫情.txt', 'r', encoding='UTF-8').readlines()
    test2 = open('数据\疫情2.txt', 'r', encoding='UTF-8').readlines()
    #test3=open('数据\测试.txt','r',encoding='UTF-8').readlines()
    #analize(test3)
    analize(text1)
    analize(test2)
    #pos12='19-12:'+feelingCal(content12)+'\n'
    message=''
    for i in range (0,len(storeDate)):
        message+=storeDate[i]+': '+feelingCal(storeData[i])+'\n'
    storeRes.write(message)
    #pos1='20-01:'+feelingCal(content1)+'\n'
    #pos2='20-02:'+feelingCal(content2)+'\n'
    #pos3='20-03:'+feelingCal(content3)+'\n'
    #pos4='20-04:'+feelingCal(content4)+'\n'
    #pos5='20-05:'+feelingCal(content5)+'\n'
    #pos6='20-06:'+feelingCal(content6)+'\n'
    #storeRes.write(pos1+pos2+pos3+pos4+pos5+pos6)
if __name__ == '__main__':
    main()
