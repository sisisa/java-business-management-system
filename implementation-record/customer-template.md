# Customer実装テンプレート

## 1. Entity

- DBテーブルとの対応を定義する
- ID・永続化項目・自動管理項目を持つ
- API入力の検証は担当しない

## 2. Repository

- `JpaRepository`を継承する
- 基本CRUDはSpring Data JPAに任せる
- Entity固有の検索だけ追加する

## 3. Request DTO

- CreateRequestとUpdateRequestを分離する
- APIから受け取る項目を限定する
- ValidationをDTOに集約する

## 4. Response DTO

- Entityを直接APIレスポンスとして公開しない
- APIとして返す項目を明示する
- Entity → Response DTOの変換方法を統一する

## 5. Service

- 業務処理を担当する
- Entityの取得・変更・保存を担当する
- 存在しないデータは共通例外へ変換する
- ControllerからRepositoryを直接呼ばない

## 6. Controller

- HTTPリクエストとレスポンスを担当する
- Validationを実行する
- 業務処理はServiceへ委譲する

## 7. Exception

- `ResourceNotFoundException`などを共通化する
- Customer固有ではなく他Entityでも再利用する

## 8. GlobalExceptionHandler

- API全体のエラー処理を一箇所へ集約する
- 404、400などのHTTPレスポンスを統一する

## 9. Test

- Service：業務処理を単体テストする
- Controller：HTTP APIとしての動作をテストする
- 正常系だけでなく、存在しないIDやValidationエラーも確認する

## 10. Project / Employeeへの展開

Customerのコードをそのままコピーするのではなく、

- ディレクトリ構成
- 責務分担
- 命名規則
- Validationの考え方
- Exceptionの扱い
- Testの観点

を実装パターンとして再利用する。

複数のEntityで本当に共通すると確認できた処理だけを、
共通クラス・共通処理として抽出する。
