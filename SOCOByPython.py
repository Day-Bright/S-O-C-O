def read_txt(filename):
    """
    读取一个源代码文件,将\n替换为空格
    输入文件名
    输出string
    """
    with open(filename, encoding='utf-8') as f:
        return f.read().replace('\n', ' '), filename.split('\\')[-1]


def read_txt_with_n(filename):
    """
    读取一个源代码文件,直接读取,没有处理\n
    输入文件名
    输出string
    """
    with open(filename, encoding='utf-8') as f:
        return f.read(), filename.split('\\')[-1]


def read_all_txt(path):
    """
    读取一个目录下所有源代码到一个dict的代码,将\n替换为空格
    input 文件名
    output {源代码编号:内容}
    **此函数性能较差,运行大约需要9s**
    """
    import os
    import glob
    files = glob.glob(os.path.join(path, '*'))
    ret_dict = {}
    for file in files:
        # with open(file, encoding='utf-8') as f:

        ret_dict[file.split('\\')[-1]] = open(file, encoding='utf-8').read().replace('\n', ' ')
    return ret_dict


def read_all_txt_with_n(path):
    """
    读取一个目录下所有源代码到一个dict的代码,直接读取,没有处理\n
    input 文件名
    output {源代码编号:内容}
    **此函数性能较差,运行大约需要9s**
    """
    import os
    import glob
    files = glob.glob(os.path.join(path, '*'))
    ret_dict = {}
    for file in files:
        # with open(file, encoding='utf-8') as f:

        ret_dict[file.split('\\')[-1]] = open(file, encoding='utf-8').read()
    return ret_dict


def load_rel(filename):
    """
    代码和作者的映射关系
    input 文件名
    output {源代码编号:作者id}
    """
    ret_dict = {}
    file = open(filename, encoding='utf-8').readlines()[1:]
    for line in file:
        uid, pid = line.split(',')
        ret_dict[pid[:-1]] = uid
    return ret_dict


def create_ngram_list(input_list):
    ngram_list = []
    if len(input_list) <= 5:
        ngram_list.append(input_list)
    else:
        for tmp in zip(*[input_list[i:] for i in range(5)]):
            tmp = "".join(tmp)
            ngram_list.append(tmp)
    return ngram_list


def score(content, query):
    content_list = create_ngram_list(content)
    query_list = create_ngram_list(query)
    tmp = list(set(content_list).intersection(set(query_list)))
    return len(tmp)


def score_all(dict_content, dict_uid, query, query_pid):
    dict_score = {}
    for pid in dict_content:
        # print(pid)
        content = dict_content[pid]
        result = score(content, query)
        dict_score[pid] = result
    max_pid = max(dict_score, key=dict_score.get)
    max_uid = dict_uid[max_pid]
    return query_pid, max_uid


def create_ngram_list_hzy(input_text, n):
    ngram_list = {}
    if len(input_text) <= n:
        ngram_list[input_text] = 1
    else:
        for one_ngram in zip(*[input_text[i:] for i in range(n)]):
            # 若ngram不存在则添加，否则值加1
            one_ngram = "".join(one_ngram)
            if ngram_list.get(one_ngram):
                ngram_list[one_ngram] += 1
            else:
                ngram_list[one_ngram] = 1
    return ngram_list


def score_hzy(query, document):
    sum = 0
    for term in query:
        if document.get(term):
            sum += min(query[term], document[term])
    return sum


def score_all_hzy(query, document, docID_userID):
    # 预处理数据，这里是处理成ngram
    for queryid in query:
        query[queryid] = create_ngram_list_hzy(query[queryid], 5)

    for documentid in document:
        document[documentid] = create_ngram_list_hzy(document[documentid], 5)
    print("ngram处理完毕")
    result_queryid_userid = {}
    counter = 0
    for queryid in query:
        print("%5s|%5s" % (counter, len(query)))
        counter += 1
        document_score = {}
        # 计算得分
        for documentid in document:
            document_score[documentid] = score_hzy(query[queryid], document[documentid])
        # 取最大
        max_documentid = max(document_score, key=document_score.get)
        # 映射到用户id
        result_queryid_userid[queryid] = docID_userID[max_documentid]
        # print(queryid)
    return result_queryid_userid


def load_labels(file_path):
    with open(file_path, 'r') as fp:
        reader = csv.reader(fp)
        labels = list(reader)

    assert (labels[0][0] == 'uid')
    assert (labels[0][1] == 'pid')

    labels = labels[1:]
    for i in range(len(labels)):
        labels[i][0] = int(labels[i][0])
        labels[i][1] = int(labels[i][1])
    return labels


if __name__ == '__main__':
    import os
    import glob
    import csv
    import argparse
    from sklearn.metrics import accuracy_score

    test_path = r"D:\soco1\data_dir\dev"
    train_path = r"D:\soco1\data_dir\merged_train"
    train_csv_file_name = r"D:\soco1\data_dir\train_m.csv"
    answer = r'./submission_sample/answer.csv'  # 答案的位置
    # result_filename = r"D:\soco1\result.csv"
    # 读入查询(测试数据)
    query = read_all_txt_with_n(test_path)
    print("加载查询完毕")
    # 读入训练数据
    document = read_all_txt_with_n(train_path)
    print("加载文档完毕")
    # 读入训练代码id和作者id
    docID_userID = load_rel(train_csv_file_name)
    print("加载映射表完毕")

    # 计算结果
    result = score_all_hzy(query, document, docID_userID)
    print("计算得分完毕")

    # 保存文件
    # files = glob.glob(os.path.join(result_filename, '*'))
    csv_file = open('tag0.csv', 'w', encoding='utf-8', newline='' "")
    csv_writer = csv.writer(csv_file)
    csv_writer.writerow(['uid', 'pid'])

    for pid, uid in result.items():
        csv_writer.writerow([uid, pid])
    csv_file.close()

    # 开始评测
    parser = argparse.ArgumentParser()
    parser.add_argument('--gold_file')
    parser.add_argument('--pred_file')
    args = parser.parse_args()

    gold = load_labels(answer)  # 答案的位置
    pred = load_labels(r'tag0.csv')  # 你的答案的位置

    assert (len(gold) == len(pred))

    gold = sorted(gold, key=lambda elem: elem[1])
    pred = sorted(pred, key=lambda elem: elem[1])

    gold = [elem[0] for elem in gold]
    pred = [elem[0] for elem in pred]

    print('Accuracy: {}'.format(accuracy_score(gold, pred)))